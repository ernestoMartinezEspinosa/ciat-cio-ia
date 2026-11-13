package mx.com.ciat.cio.market.platform.marketstreamservice.domain.model;

public record TickData(
        String symbol,
        double price,
        long timestampMs, // Yahoo envía milisegundos
        long volume,
        double dayHigh,
        double dayLow,
        String sector,           
        double sectorStrength    
) {}