package mx.com.ciat.cio.market.platform.marketalertservice.infrastructure.adapters.in.streams;


import mx.com.ciat.cio.market.platform.marketalertservice.domain.model.Signal;

import mx.com.ciat.cio.market.platform.marketalertservice.domain.model.TickData;

import org.apache.kafka.common.serialization.Serdes;

import org.apache.kafka.streams.StreamsBuilder;

import org.apache.kafka.streams.kstream.*;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.annotation.EnableKafkaStreams;

import org.springframework.kafka.support.serializer.JsonSerde;


import java.math.BigDecimal;

import java.time.Instant;


@Configuration

@EnableKafkaStreams

public class TradingEvaluationTopology {


    @Bean

    public KStream<String, TickData> processTicks(StreamsBuilder builder) {

        // 1. Leer el tópico de ticks

        KStream<String, TickData> ticks = builder.stream("market-ticks",

                Consumed.with(Serdes.String(), new JsonSerde<>(TickData.class)));


        // 2. Evaluar el Score en tiempo real (Manual PRO+)

        KStream<String, Signal> signals = ticks

                .mapValues((key, tick) -> evaluateTick(tick))

                .filter((key, signal) -> signal != null); // Solo pasan las señales válidas


        // 3. Publicar la señal al tópico final

        signals.to("market-signals", Produced.with(Serdes.String(), new JsonSerde<>(Signal.class)));


        return ticks;

    }


    private Signal evaluateTick(TickData tick) {

        // Filtro de Integridad de Datos (Cap. 7.5)

        if (tick.price() <= 0 || tick.price() > 5000) {

            return null;

        }


        // Cálculo del Score (Cap. 15) - Versión simplificada

        int score = 0;

        

        // RVOL (20 pts)

        if (tick.volume() > 10_000_000) score += 20;

        else if (tick.volume() > 5_000_000) score += 16;


        // VWAP (15 pts) - Dummy

        score += 15; 

        // Catalizador (20 pts) - Dummy

        score += 15;

        // Momentum (15 pts)

        score += 15;

        // --- 5. SECTOR (15 pts) - DINÁMICO ---
        // Usamos la fuerza del sector que viene desde Redis (0.0 a 1.0)
        // Si el sector tiene fuerza 0.85, le damos los 15 puntos completos.
        // Si tiene 0.5, le damos la mitad (7.5 puntos -> 8 puntos redondeados).
        int sectorPoints = (int) Math.round(tick.sectorStrength() * 15);
        score += sectorPoints;


        // Solo generamos señal si el Score es >= 70 (Cap. 14)

        if (score < 70) {

            return null;

        }


        // Cálculo de R (Cap. 6 y 10)

        BigDecimal currentPrice = BigDecimal.valueOf(tick.price());

        BigDecimal stopLoss = currentPrice.multiply(new BigDecimal("0.98")); // 2% de stop

        BigDecimal risk = currentPrice.subtract(stopLoss);

        BigDecimal target1R = currentPrice.add(risk);

        BigDecimal target2R = currentPrice.add(risk.multiply(new BigDecimal("2")));


        return new Signal(

                tick.symbol(),

                "BUY",

                currentPrice,

                Instant.ofEpochMilli(tick.timestampMs()),

                score,

                stopLoss,

                target1R,

                target2R,

                "Score: " + score + " | Volumen: " + tick.volume()

        );

    }

}

