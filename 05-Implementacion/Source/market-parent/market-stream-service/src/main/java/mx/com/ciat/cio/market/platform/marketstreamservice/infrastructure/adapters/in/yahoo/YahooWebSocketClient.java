package mx.com.ciat.cio.market.platform.marketstreamservice.infrastructure.adapters.in.yahoo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketstreamservice.application.service.CatalogCacheService;
import mx.com.ciat.cio.market.platform.marketstreamservice.domain.model.TickData;
import mx.com.ciat.cio.market.platform.marketstreamservice.domain.port.in.ProcessTickUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import quotefeeder.PricingData;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class YahooWebSocketClient {

    private static final String YAHOO_WS_URL = "wss://streamer.finance.yahoo.com/?version=2";
    private static final String KAFKA_TOPIC = "market-ticks";
    private static final Duration INITIAL_RETRY_DELAY = Duration.ofSeconds(5);
    private static final Duration MAX_RETRY_DELAY = Duration.ofMinutes(1);

    private final ProcessTickUseCase processTickUseCase;
    private final KafkaTemplate<String, TickData> kafkaTemplate;
    private final CatalogCacheService catalogCacheService;
    private final String symbols;
    private final List<String> symbolList;
    private final WebSocketClient client;
    private final ObjectMapper objectMapper;

    private final Map<String, Long> lastTimestamps = new ConcurrentHashMap<>();
    private final AtomicBoolean connecting = new AtomicBoolean(false);

    public YahooWebSocketClient(
            ProcessTickUseCase processTickUseCase,
            KafkaTemplate<String, TickData> kafkaTemplate,
            CatalogCacheService catalogCacheService,
            @Value("${yahoo.websocket.symbols}") String symbols) {

        this.processTickUseCase = processTickUseCase;
        this.kafkaTemplate = kafkaTemplate;
        this.catalogCacheService = catalogCacheService;
        this.symbols = symbols;
        this.symbolList = Arrays.stream(symbols.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .toList();

        this.client = new ReactorNettyWebSocketClient();
        this.objectMapper = new ObjectMapper();

        log.info("YahooWebSocketClient inicializado. Símbolos: {}", this.symbolList);
        log.info("🔌 KafkaTemplate inyectado: {}", (kafkaTemplate != null ? "OK" : "NULL"));
    }

    public void connect() {
        if (!connecting.compareAndSet(false, true)) {
            log.warn("Ya existe una conexión WebSocket activa o en proceso.");
            return;
        }

        URI uri = URI.create(YAHOO_WS_URL);
        log.info("Iniciando conexión WebSocket con Yahoo Finance: {}", uri);

        client.execute(uri, session -> {
            String subscribeMsg = buildSubscriptionMessage();
            log.info("Enviando suscripción a Yahoo Finance: {}", subscribeMsg);

            Mono<Void> sendSubscription = session.send(Mono.just(session.textMessage(subscribeMsg)));

            Mono<Void> receiveMessages = session.receive()
                    .map(WebSocketMessage::getPayloadAsText)
                    .concatMap(payload -> 
                        Mono.fromRunnable(() -> processMessage(payload))
                            .subscribeOn(Schedulers.boundedElastic())
                    )
                    .then()
                    .then(Mono.error(new IllegalStateException("Conexión WebSocket cerrada por Yahoo Finance.")));

            return sendSubscription.then(receiveMessages);
        })
        .retryWhen(reactor.util.retry.Retry.backoff(Long.MAX_VALUE, INITIAL_RETRY_DELAY)
                .maxBackoff(MAX_RETRY_DELAY)
                .doBeforeRetry(signal -> log.warn("Reconectando con Yahoo... Intento #{}", signal.totalRetries() + 1))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure())
        )
        .doOnError(error -> log.error("Fallo crítico en la conexión WebSocket: {}", error.getMessage(), error))
        .doFinally(signal -> {
            connecting.set(false);
            log.info("Ciclo WebSocket finalizado. Señal: {}", signal);
        })
        .subscribe(
                null,
                error -> log.error("Error terminal en Yahoo WebSocket.", error)
        );
    }

    private String buildSubscriptionMessage() {
        return "{\"subscribe\":[" + symbolList.stream().map(s -> "\"" + s + "\"").reduce((a, b) -> a + "," + b).orElse("") + "]}";
    }

    private void processMessage(String jsonPayload) {
        try {
            JsonNode root = objectMapper.readTree(jsonPayload);
            JsonNode messageNode = root.get("message");

            if (messageNode == null || messageNode.isNull()) {
                log.debug("Mensaje de control recibido: {}", jsonPayload);
                return;
            }

            byte[] decodedBytes = Base64.getDecoder().decode(messageNode.asText());
            PricingData pricingData = PricingData.parseFrom(decodedBytes);

            if (pricingData.getQuoteType() != PricingData.QuoteType.EQUITY || pricingData.getPrice() <= 0) {
                return;
            }

            String symbol = pricingData.getId();
            long timestamp = pricingData.getTime();

            // Idempotencia
            Long lastTimestamp = lastTimestamps.get(symbol);
            if (lastTimestamp != null && timestamp <= lastTimestamp) {
                return;
            }

            // Volumen (fallback)
            long volume = pricingData.getDayVolume();
            if (volume <= 0) volume = pricingData.getVol24Hr();
            if (volume <= 0) volume = pricingData.getLastSize();
            
            // 👇 VALIDACIÓN CRÍTICA: Si el volumen es 0, asignamos 1 para no corromper VWAP
            if (volume <= 0) {
                log.debug("⚠️ Tick con volumen 0 para {}. Se asigna volumen 1 para no corromper VWAP.", symbol);
                volume = 1; // Volumen mínimo para que el tick cuente para el precio
            }

            // Consultar sector y fuerza desde Redis
            String sector = catalogCacheService.getSector(symbol);
            double sectorStrength = catalogCacheService.getSectorStrength(symbol);

            TickData tick = new TickData(
                    symbol,
                    pricingData.getPrice(),
                    timestamp,
                    volume,
                    pricingData.getDayHigh(),
                    pricingData.getDayLow(),
                    sector,
                    sectorStrength
            );

            // Publicar en Kafka
            kafkaTemplate.send(KAFKA_TOPIC, tick.symbol(), tick)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("❌ Error enviando tick a Kafka para {}: {}", tick.symbol(), ex.getMessage());
                        } else {
                            log.info("📤 Tick publicado: {} | Sector: {} | Fuerza: {} | Vol: {} | Offset: {}", 
                                    tick.symbol(), 
                                    tick.sector(), 
                                    tick.sectorStrength(),
                                    tick.volume(),
                                    result.getRecordMetadata().offset());
                        }
                    });

            // Persistencia asíncrona
            Mono.fromRunnable(() -> processTickUseCase.process(tick))
                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe();

            // Actualizar timestamp
            lastTimestamps.put(symbol, timestamp);

        } catch (Exception e) {
            log.error("Error procesando mensaje de Yahoo: {}", e.getMessage(), e);
        }
    }
}