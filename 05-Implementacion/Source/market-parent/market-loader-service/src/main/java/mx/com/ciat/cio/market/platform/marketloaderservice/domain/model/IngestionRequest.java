package mx.com.ciat.cio.market.platform.marketloaderservice.domain.model;

import java.time.Instant;

public record IngestionRequest(

        String sourceName,

        Long instrumentId,

        String ticker,

        String timeframe,

        Instant requestedFrom,

        Instant requestedTo

) {}