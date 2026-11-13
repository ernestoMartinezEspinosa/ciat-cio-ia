package mx.com.ciat.cio.market.platform.marketstreamservice.infrastructure.adapters.out.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketstreamservice.domain.model.TickData;
import mx.com.ciat.cio.market.platform.marketstreamservice.domain.port.out.TickPersistencePort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class MySqlTickPersistenceAdapter implements TickPersistencePort {

    private final CandleJpaRepository candleRepository;

    @Override
    public void saveTick(TickData tick) {
        try {
            // 1. Calcular los tiempos de la vela (minuto exacto)
            Instant tickTime = Instant.ofEpochMilli(tick.timestampMs());
            Instant openTime = tickTime.truncatedTo(ChronoUnit.MINUTES);
            Instant closeTime = openTime.plus(1, ChronoUnit.MINUTES);

            // 2. Convertir el precio (double) a BigDecimal para la entidad
            BigDecimal price = BigDecimal.valueOf(tick.price());

            // 3. Ejecutar el UPSERT en la base de datos
            candleRepository.upsertMinuteCandle(
                    tick.symbol(),
                    openTime,
                    closeTime,
                    price,
                    tick.volume()
            );

            log.debug("✅ UPSERT exitoso para {} en minuto {}", tick.symbol(), openTime);

        } catch (Exception e) {
            // Logueamos el error pero NO lo lanzamos para no detener el flujo del WebSocket
            log.error("❌ Error persistiendo tick en MySQL para {}: {}", 
                    tick.symbol(), e.getMessage(), e);
        }
    }
}