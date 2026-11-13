package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "candles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_candle",
                        columnNames = {
                                "instrument_id",
                                "timeframe",
                                "open_time"
                        }
                )
        },
        indexes = {

                @Index(
                        name = "idx_candle_lookup",
                        columnList = "instrument_id,timeframe,open_time"
                ),

                @Index(
                        name = "idx_candle_incremental",
                        columnList = "instrument_id,open_time"
                )

        }
)
public class CandleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instrument_id", nullable = false)
    private Long instrumentId;

    @Column(nullable = false, length = 10)
    private String timeframe;

    @Column(name = "open_time", nullable = false)
    private Instant openTime;

    @Column(name = "close_time", nullable = false)
    private Instant closeTime;

    @Column(name = "open_price", nullable = false, precision = 18, scale = 6)
    private BigDecimal openPrice;

    @Column(name = "high_price", nullable = false, precision = 18, scale = 6)
    private BigDecimal highPrice;

    @Column(name = "low_price", nullable = false, precision = 18, scale = 6)
    private BigDecimal lowPrice;

    @Column(name = "close_price", nullable = false, precision = 18, scale = 6)
    private BigDecimal closePrice;

    @Column(nullable = false)
    private Long volume = 0L;

    @Column(name = "trades_count", nullable = false)
    private Integer tradesCount = 0;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public CandleEntity() {
    }

    @PrePersist
    public void prePersist() {

        if (createdAt == null) {
            createdAt = Instant.now();
        }

        if (volume == null) {
            volume = 0L;
        }

        if (tradesCount == null) {
            tradesCount = 0;
        }
    }

    public Long getId() {
        return id;
    }

    public Long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
    }

    public Instant getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Instant openTime) {
        this.openTime = openTime;
    }

    public Instant getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Instant closeTime) {
        this.closeTime = closeTime;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Integer getTradesCount() {
        return tradesCount;
    }

    public void setTradesCount(Integer tradesCount) {
        this.tradesCount = tradesCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}