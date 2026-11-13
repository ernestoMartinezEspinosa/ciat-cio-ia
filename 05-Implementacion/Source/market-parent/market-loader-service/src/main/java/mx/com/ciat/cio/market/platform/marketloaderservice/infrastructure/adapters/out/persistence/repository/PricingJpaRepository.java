package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository;

import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity.PricingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PricingJpaRepository
        extends JpaRepository<PricingEntity, Long> {

    Optional<PricingEntity> findTopByInstrumentIdOrderByEventTimeDesc(
            Long instrumentId
    );

}