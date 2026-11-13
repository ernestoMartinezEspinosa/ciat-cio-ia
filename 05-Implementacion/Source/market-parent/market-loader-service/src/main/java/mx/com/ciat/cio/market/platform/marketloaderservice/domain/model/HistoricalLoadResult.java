package mx.com.ciat.cio.market.platform.marketloaderservice.domain.model;

import java.time.Instant;

import mx.com.ciat.cio.market.platform.marketloaderservice.application.service.execution.HistoricalExecutionResult;

public record HistoricalLoadResult(

        String ticker,

        HistoricalLoadStatus status,

        Instant from,

        Instant to,

        int candlesDownloaded,

        int candlesPersisted,

        long executionTimeMs

) implements HistoricalExecutionResult {

    @Override
    public long downloadedRecords() {
        return candlesDownloaded;
    }

    @Override
    public long persistedRecords() {
        return candlesPersisted;
    }

    public boolean success() {
        return status == HistoricalLoadStatus.SUCCESS;
    }

}