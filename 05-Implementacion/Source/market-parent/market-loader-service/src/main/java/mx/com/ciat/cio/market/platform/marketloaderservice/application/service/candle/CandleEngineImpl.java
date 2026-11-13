package mx.com.ciat.cio.market.platform.marketloaderservice.application.service.candle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.service.CandleService;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.HistoricalDownload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CandleEngineImpl implements CandleEngine {

    private final CandleService candleService;

    @Override
    public CandlePersistenceResult process(
            HistoricalDownload download
    ) {

        if (download == null) {

            log.warn("HistoricalDownload is null.");

            return CandlePersistenceResult.empty();
        }

        log.info(
        	    "Processing {} candles for instrument={}",
        	    download.candles().size(),
        	    download.instrument().ticker()
        	);

        CandlePersistenceResult result =
                candleService.process(
                        download.candles()
                );

        log.info(
                "Candles processed downloaded={} persisted={}",
                result.downloaded(),
                result.persisted()
        );

        return result;
    }
}