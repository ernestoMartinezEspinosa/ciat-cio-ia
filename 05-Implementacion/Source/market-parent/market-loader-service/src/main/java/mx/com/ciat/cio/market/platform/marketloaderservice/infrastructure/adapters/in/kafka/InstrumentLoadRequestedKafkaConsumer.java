package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.in.kafka;

import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.event.InstrumentLoadRequestedEvent;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.in.InstrumentLoadRequestedUseCase;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out.EventIdempotencyPort;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InstrumentLoadRequestedKafkaConsumer {

    private final InstrumentLoadRequestedUseCase useCase;
    private final EventIdempotencyPort idempotencyPort;

    public InstrumentLoadRequestedKafkaConsumer(
            InstrumentLoadRequestedUseCase useCase,
            EventIdempotencyPort idempotencyPort
    ) {
        this.useCase = useCase;
        this.idempotencyPort = idempotencyPort;
    }

    @KafkaListener(
    		topics = "${spring.kafka.topics.instrument-load-requested}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(
            InstrumentLoadRequestedEvent event,
            Acknowledgment ack,
            MessageHeaders headers
    ) {
    	String key = (String) headers.get(KafkaHeaders.RECEIVED_KEY);
        if (event == null) {
            log.warn("⚠️ Null event received key={}", key);
            ack.acknowledge();
            return;
        }

        log.info("📩 Event received key={} eventId={} symbol={}",
                key,
                event.eventId(),
                event.symbol()
        );

        try {

        	log.info(
        		    "Received eventId={} correlationId={}",
        		    event.eventId(),
        		    event.correlationId()
        		);
            // 🔐 IDPOTENCIA (CRÍTICO)
            if (idempotencyPort.isProcessed(event.eventId())) {
                log.warn("♻️ Duplicate event ignored eventId={}", event.eventId());
                ack.acknowledge();
                return;
            }

            useCase.execute(event);

            idempotencyPort.markAsProcessed(
                    event.eventId(),
                    event.correlationId()
            );

            ack.acknowledge();

            log.info("✅ Event processed eventId={}", event.eventId());

        } catch (Exception ex) {

            log.error("❌ Error processing eventId={} error={}",
                    event.eventId(),
                    ex.getMessage(),
                    ex
            );

            // ❗ NO ACK -> activa retry/DLQ
            throw ex;
        }
    }
}