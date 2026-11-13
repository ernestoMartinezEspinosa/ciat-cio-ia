package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository;

import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity.CandleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CandleJpaRepository
        extends JpaRepository<CandleEntity, Long> {
	@Query("""
			select max(c.openTime)
			from CandleEntity c
			where c.instrumentId=:instrumentId
			and c.timeframe=:timeframe
			""")
			Instant findLastLoadedTime(
			        Long instrumentId,
			        String timeframe
			);

    Optional<CandleEntity> findTopByInstrumentIdAndTimeframeOrderByOpenTimeDesc(
            Long instrumentId,
            String timeframe
    );

    List<CandleEntity> findByInstrumentIdAndTimeframeOrderByOpenTimeAsc(
            Long instrumentId,
            String timeframe
    );

    boolean existsByInstrumentIdAndTimeframeAndOpenTime(
            Long instrumentId,
            String timeframe,
            Instant openTime
    );

}