package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository;

import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity.IngestionControlEntity;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity.IngestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngestionControlJpaRepository
        extends JpaRepository<IngestionControlEntity, Long> {

    List<IngestionControlEntity> findTop100ByOrderByExecutionStartDesc();

    List<IngestionControlEntity> findByStatus(IngestionStatus status);

    List<IngestionControlEntity> findByInstrumentIdOrderByExecutionStartDesc(
            Long instrumentId
    );

}