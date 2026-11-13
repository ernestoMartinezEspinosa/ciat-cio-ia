package mx.com.ciat.cio.market.platform.marketloaderservice.application.service.execution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.command.HistoricalLoadCommand;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.service.IngestionControlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class HistoricalExecutionEngineImpl
        implements HistoricalExecutionEngine {

    private final IngestionControlService ingestionControlService;

    @Override
    public <T> T execute(

            HistoricalLoadCommand command,

            HistoricalExecutionCallback<T> callback

    ) {

        Long executionId = null;

        try {

            executionId =
                    ingestionControlService.start(command);

            T result =
                    callback.execute();

            if (result instanceof HistoricalExecutionResult executionResult) {

                ingestionControlService.success(

                        executionId,

                        executionResult.downloadedRecords(),

                        executionResult.persistedRecords()

                );

            } else {

                ingestionControlService.success(
                        executionId,
                        0,
                        0
                );

            }

            return result;

        } catch (Exception ex) {

            if (executionId != null) {

                ingestionControlService.error(
                        executionId,
                        ex
                );

            }

            throw ex;
        }

    }

}