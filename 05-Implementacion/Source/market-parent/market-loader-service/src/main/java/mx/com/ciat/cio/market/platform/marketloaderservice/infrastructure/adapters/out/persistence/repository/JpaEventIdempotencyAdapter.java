package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository;

import org.springframework.stereotype.Repository;

import mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out.EventIdempotencyPort;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity.ProcessedEventEntity;

import java.util.UUID;

@Repository
public class JpaEventIdempotencyAdapter implements EventIdempotencyPort {

    private final ProcessedEventJpaRepository repository;

    public JpaEventIdempotencyAdapter(ProcessedEventJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isProcessed(UUID eventId) {
        return repository.existsById(eventId);
    }

    @Override
    public void markAsProcessed(UUID eventId, UUID correlationId) {
        repository.save(new ProcessedEventEntity(eventId, correlationId));
    }
}