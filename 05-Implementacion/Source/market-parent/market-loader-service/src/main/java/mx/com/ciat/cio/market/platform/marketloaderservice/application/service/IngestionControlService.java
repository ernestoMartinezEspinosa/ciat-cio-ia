package mx.com.ciat.cio.market.platform.marketloaderservice.application.service;

import lombok.RequiredArgsConstructor;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.command.HistoricalLoadCommand;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out.IngestionControlPort;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngestionControlService {

    private final IngestionControlPort port;


    public Long start(HistoricalLoadCommand request) {

        return port.start(request);
    }


    public void success(
            Long executionId,
            long downloadedRecords,
            long persistedRecords
    ) {

        port.success(
                executionId,
                downloadedRecords,
                persistedRecords
        );
    }


    public void error(
            Long executionId,
            Exception exception
    ) {

        port.error(
                executionId,
                exception
        );
    }
}