package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository;

import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessedEventJpaRepository
        extends JpaRepository<ProcessedEventEntity, UUID> {

    boolean existsByEventId(UUID eventId);

}