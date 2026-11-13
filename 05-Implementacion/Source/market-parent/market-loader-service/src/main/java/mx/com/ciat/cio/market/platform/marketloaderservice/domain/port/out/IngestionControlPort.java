package mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out;

import mx.com.ciat.cio.market.platform.marketloaderservice.application.command.HistoricalLoadCommand;

public interface IngestionControlPort {

    Long start(HistoricalLoadCommand request);

    void success(
            Long executionId,
            long downloadedRecords,
            long persistedRecords
    );

    void error(
            Long executionId,
            Exception exception
    );

}