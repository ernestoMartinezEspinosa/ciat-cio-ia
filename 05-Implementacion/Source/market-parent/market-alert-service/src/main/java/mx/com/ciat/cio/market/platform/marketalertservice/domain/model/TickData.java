package mx.com.ciat.cio.market.platform.marketalertservice.domain.model;


public record TickData(

        String symbol,

        double price,

        long timestampMs,

        long volume,

        double dayHigh,

        double dayLow,
        String sector,           
        double sectorStrength    

) {}
