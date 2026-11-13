package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository;

import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity.InstrumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstrumentJpaRepository
        extends JpaRepository<InstrumentEntity, Long> {

    Optional<InstrumentEntity> findByTicker(String ticker);

    boolean existsByTicker(String ticker);
    
    Optional<InstrumentEntity> findByTickerAndExchangeName(
            String ticker,
            String exchange
    );

}