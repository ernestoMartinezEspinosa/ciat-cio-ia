package mx.com.ciat.cio.market.platform.marketloaderservice.application.service.execution;

public interface HistoricalExecutionResult {

    long downloadedRecords();

    long persistedRecords();

}