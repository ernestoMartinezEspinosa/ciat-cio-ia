package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "pricing",
        indexes = {

                @Index(
                        name = "idx_pricing_symbol_time",
                        columnList = "instrument_id,event_time"
                ),

                @Index(
                        name = "idx_pricing_received",
                        columnList = "received_at"
                )
        }
)
public class PricingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instrument_id", nullable = false)
    private Long instrumentId;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal price;

    @Column(name = "change_amount", precision = 18, scale = 6)
    private BigDecimal changeAmount;

    @Column(name = "change_percent", precision = 18, scale = 6)
    private BigDecimal changePercent;

    @Column(name = "market_hours")
    private Integer marketHours;

    @Column(name = "quote_type")
    private Integer quoteType;

    @Column(name = "event_time", nullable = false)
    private Instant eventTime;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    protected PricingEntity() {
    }

    @PrePersist
    public void prePersist() {

        if (receivedAt == null) {
            receivedAt = Instant.now();
        }

        if (eventTime == null) {
            eventTime = Instant.now();
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(BigDecimal changePercent) {
        this.changePercent = changePercent;
    }

    public Integer getMarketHours() {
        return marketHours;
    }

    public void setMarketHours(Integer marketHours) {
        this.marketHours = marketHours;
    }

    public Integer getQuoteType() {
        return quoteType;
    }

    public void setQuoteType(Integer quoteType) {
        this.quoteType = quoteType;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

}