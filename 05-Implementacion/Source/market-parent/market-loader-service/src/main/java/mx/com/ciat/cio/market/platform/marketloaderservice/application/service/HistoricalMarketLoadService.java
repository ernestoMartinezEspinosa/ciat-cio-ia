package mx.com.ciat.cio.market.platform.marketloaderservice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.command.HistoricalLoadCommand;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.service.candle.CandleEngine;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.service.candle.CandlePersistenceResult;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.service.download.HistoricalDownloadEngine;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.service.execution.HistoricalExecutionEngine;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.service.instrument.InstrumentEngine;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.HistoricalDownload;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.HistoricalLoadResult;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.HistoricalLoadStatus;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Instrument;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class HistoricalMarketLoadService {

    private final HistoricalExecutionEngine executionEngine;

    private final InstrumentEngine instrumentEngine;

    private final HistoricalDownloadEngine historicalDownloadEngine;

    private final CandleEngine candleEngine;

    @Transactional
    public HistoricalLoadResult load(
            HistoricalLoadCommand command
    ) {

        return executionEngine.execute(command, () -> {

            long startTime = System.currentTimeMillis();

            log.info(
                    "Starting historical load ticker={} exchange={} interval={}",
                    command.ticker(),
                    command.exchange(),
                    command.interval()
            );

            Instrument instrument =
                    instrumentEngine.getOrCreate(
                            command.ticker(),
                            command.exchange()
                    );

            HistoricalDownload download =
                    historicalDownloadEngine.download(
                            instrument,
                            command.interval(),
                            command.from(),
                            command.to()
                    );

            CandlePersistenceResult persistence =
                    candleEngine.process(download);

            instrumentEngine.updateHistoricalLoad(
                    instrument,
                    download
            );

            log.info(
                    "Historical load completed ticker={} downloaded={} persisted={}",
                    command.ticker(),
                    persistence.downloaded(),
                    persistence.persisted()
            );

            return new HistoricalLoadResult(
                    command.ticker(),
                    HistoricalLoadStatus.SUCCESS,
                    command.from(),
                    command.to(),
                    (int) persistence.downloaded(),
                    (int) persistence.persisted(),
                    System.currentTimeMillis() - startTime
            );

        });

    }

}