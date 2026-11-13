package mx.com.ciat.cio.market.platform.marketloaderservice.application.service.execution;

import mx.com.ciat.cio.market.platform.marketloaderservice.application.command.HistoricalLoadCommand;

public interface HistoricalExecutionEngine {

    <T> T execute(
            HistoricalLoadCommand command,
            HistoricalExecutionCallback<T> callback
    );

}