package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "instrument",
        indexes = {
                @Index(name = "idx_instrument_ticker", columnList = "ticker"),
                @Index(name = "idx_instrument_last_load", columnList = "last_historical_load")
        }
)
@Getter
@Setter
public class InstrumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String ticker;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "exchange_name", length = 50)
    private String exchangeName;

    // 👇 NUEVA RELACIÓN CON SECTOR_STRENGTH
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id") // La columna en la BD
    private SectorStrengthEntity sector;

    @Column(length = 150)
    private String industry;

    @Column(name = "market_cap")
    private Long marketCap;

    @Column(length = 10)
    private String currency;

    @Column(length = 50)
    private String timezone;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "last_historical_load")
    private Instant lastHistoricalLoad;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    public InstrumentEntity() {
    }

    public InstrumentEntity(
            Long id,
            String ticker,
            String companyName,
            String exchangeName,
            SectorStrengthEntity sector, // 👈 Cambiado el tipo
            String industry,
            Long marketCap,
            String currency,
            String timezone,
            boolean active,
            Instant lastHistoricalLoad,
            Instant createdAt) {

        this.id = id;
        this.ticker = ticker;
        this.companyName = companyName;
        this.exchangeName = exchangeName;
        this.sector = sector;
        this.industry = industry;
        this.marketCap = marketCap;
        this.currency = currency;
        this.timezone = timezone;
        this.active = active;
        this.lastHistoricalLoad = lastHistoricalLoad;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}