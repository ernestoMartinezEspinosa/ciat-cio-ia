package mx.com.ciat.cio.market.platform.marketloaderservice.application.usecase;

import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.command.HistoricalLoadCommand;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.service.HistoricalMarketLoadService;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.event.InstrumentLoadRequestedEvent;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.in.InstrumentLoadRequestedUseCase;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out.EventIdempotencyPort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class InstrumentLoadRequestedUseCaseImpl implements InstrumentLoadRequestedUseCase {

	private final EventIdempotencyPort idempotencyPort;
    private final HistoricalMarketLoadService historicalMarketLoadService;

    public InstrumentLoadRequestedUseCaseImpl(
            EventIdempotencyPort idempotencyPort,
            HistoricalMarketLoadService historicalMarketLoadService) {

        this.idempotencyPort = idempotencyPort;
        this.historicalMarketLoadService = historicalMarketLoadService;
    }

    @Override
    @Transactional
    public void execute(InstrumentLoadRequestedEvent event) {

        UUID eventId = event.eventId();
        UUID correlationId = event.correlationId();

        log.info("🚀 Starting processing eventId={} correlationId={} symbol={}",
                eventId,
                correlationId,
                event.symbol()
        );

        // =========================
        // 1. IDEMPOTENCIA (GUARD)
        // =========================
        if (idempotencyPort.isProcessed(eventId)) {
            log.warn("♻️ Event already processed eventId={}", eventId);
            return;
        }

        try {

            // =========================
            // 2. BUSINESS LOGIC (CORE)
            // =========================
            processInstrumentLoad(event);

            // =========================
            // 3. MARK AS PROCESSED
            // =========================
            idempotencyPort.markAsProcessed(eventId, correlationId);

            log.info("✅ Event successfully processed eventId={}", eventId);

        } catch (Exception ex) {

            log.error("❌ Error processing eventId={} symbol={} error={}",
                    eventId,
                    event.symbol(),
                    ex.getMessage(),
                    ex
            );

            // ❗ IMPORTANTE:
            // No marcamos como procesado para permitir retry/DLQ
            throw ex;
        }
    }

    /**
     * Lógica de negocio principal.
     * Aquí deberías conectar:
     * - APIs externas (Matrixx, etc.)
     * - Persistencia
     * - Transformaciones
     */
    private void processInstrumentLoad(InstrumentLoadRequestedEvent event) {

        log.info("🔄 Processing instrument load symbol={} exchange={} interval={} from={} to={}",
                event.symbol(),
                event.exchange(),
                event.interval(),
                event.from(),
                event.to()
        );

        // =========================
        // PLACEHOLDER BUSINESS LOGIC
        // =========================
        HistoricalLoadCommand command =
                new HistoricalLoadCommand(
                		null,

                        event.symbol(),

                        event.exchange(),

                        event.interval(),

                        event.from(),

                        event.to()
                );

        historicalMarketLoadService.load(command);
    }
}