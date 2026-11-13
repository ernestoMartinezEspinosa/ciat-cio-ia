package mx.com.ciat.cio.market.platform.marketstreamservice.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Repository
public interface CandleJpaRepository extends JpaRepository<CandleEntity, Long> {

    /**
     * Realiza un UPSERT (Insertar si no existe, actualizar si existe) de una vela de 1 minuto.
     * 
     * - Si es el primer tick del minuto: Inserta la vela con open, high, low, close iguales al precio actual.
     * - Si la vela ya existe: Actualiza high, low, close y suma el volumen.
     */
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO candles (
            instrument_id, timeframe, open_time, close_time, 
            open_price, high_price, low_price, close_price, 
            volume, trades_count, created_at
        ) 
        SELECT 
            i.id, '1m', :openTime, :closeTime, 
            :price, :price, :price, :price, 
            :volume, 0, NOW()
        FROM instrument i 
        WHERE i.ticker = :symbol
        ON DUPLICATE KEY UPDATE 
            high_price = GREATEST(high_price, VALUES(high_price)),
            low_price = LEAST(low_price, VALUES(low_price)),
            close_price = VALUES(close_price),
            volume = volume + VALUES(volume),
            trades_count = trades_count + 1
        """, nativeQuery = true)
    void upsertMinuteCandle(
            @Param("symbol") String symbol,
            @Param("openTime") Instant openTime,
            @Param("closeTime") Instant closeTime,
            @Param("price") BigDecimal price,
            @Param("volume") Long volume
    );
}