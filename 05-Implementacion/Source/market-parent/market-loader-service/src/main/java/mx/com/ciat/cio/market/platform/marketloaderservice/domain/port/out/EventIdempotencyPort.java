package mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out;

import java.util.UUID;

public interface EventIdempotencyPort {
    boolean isProcessed(UUID eventId);
    void markAsProcessed(UUID eventId, UUID correlationId);
}