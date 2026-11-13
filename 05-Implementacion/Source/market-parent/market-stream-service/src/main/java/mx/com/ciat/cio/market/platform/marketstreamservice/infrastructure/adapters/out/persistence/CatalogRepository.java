package mx.com.ciat.cio.market.platform.marketstreamservice.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogRepository extends JpaRepository<CandleEntity, Long> {

    @Query(value = """
        SELECT i.ticker, ss.sector, ss.strength_score 
        FROM instrument i
        LEFT JOIN sector_strength ss ON i.sector_id = ss.id
        WHERE i.active = 1
    """, nativeQuery = true)
    List<Object[]> findInstrumentsWithSectorStrength();
}