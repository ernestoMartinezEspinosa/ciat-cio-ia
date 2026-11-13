package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.ingestion;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.command.HistoricalLoadCommand;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out.IngestionControlPort;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.IngestionControlJpaRepository;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity.IngestionControlEntity;


@Component
@RequiredArgsConstructor
public class IngestionControlAdapter implements IngestionControlPort {


    private final IngestionControlJpaRepository repository;


    @Override
    @Transactional
    public Long start(HistoricalLoadCommand request) {

        IngestionControlEntity entity =
                new IngestionControlEntity();

        entity.setSourceName(request.ticker());
        entity.setInstrumentId(request.instrumentId());
        entity.setRequestedFrom(request.from());
        entity.setRequestedTo(request.to());

        return repository.save(entity)
                .getId();
    }


    @Override
    @Transactional
    public void success(
    		Long executionId,
            long downloadedRecords,
            long persistedRecords) {


        IngestionControlEntity entity =
                repository.findById(executionId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Ingestion execution not found: "
                                                + executionId
                                )
                        );


        entity.finishSuccess(
        		downloadedRecords,
        		persistedRecords
        );


        repository.save(entity);
    }


    @Override
    @Transactional
    public void error(
            Long executionId,
            Exception exception
    		) {


        IngestionControlEntity entity =
                repository.findById(executionId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Ingestion execution not found: "
                                                + executionId
                                )
                        );


        entity.finishError(
                new RuntimeException(exception)
        );


        repository.save(entity);
    }

}