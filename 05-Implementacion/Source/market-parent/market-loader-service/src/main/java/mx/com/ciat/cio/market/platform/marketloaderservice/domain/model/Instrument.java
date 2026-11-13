package mx.com.ciat.cio.market.platform.marketloaderservice.domain.model;

import java.time.Instant;

public record Instrument(
        Long id,
        String ticker,
        String companyName,
        String exchangeName,
        Long sectorId,
        String sectorName,
        String industry,
        Long marketCap,
        String currency,
        String timezone,
        boolean active,
        Instant lastHistoricalLoad,
        Instant createdAt
) {

    // Constructor de fábrica (el que ya tenías)
    public static Instrument create(String ticker, String exchangeName) {
        return new Instrument(
                null, ticker, null, exchangeName, null, null, null, null,
                "USD", "UTC", true, null, Instant.now()
        );
    }

    // 👇 NUEVO MÉTODO: Devuelve una copia con la fecha actualizada
    public Instrument withLastHistoricalLoad(Instant newLastHistoricalLoad) {
        return new Instrument(
                this.id,
                this.ticker,
                this.companyName,
                this.exchangeName,
                this.sectorId,
                this.sectorName,
                this.industry,
                this.marketCap,
                this.currency,
                this.timezone,
                this.active,
                newLastHistoricalLoad, // 👈 El campo que cambiamos
                this.createdAt
        );
    }
}