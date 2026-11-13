package mx.com.ciat.cio.market.platform.marketloaderservice.application.service.execution;

@FunctionalInterface
public interface HistoricalExecutionCallback<T> {

    T execute();

}