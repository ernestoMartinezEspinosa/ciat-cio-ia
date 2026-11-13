package mx.com.ciat.cio.market.platform.marketloaderservice.application.service.candle;

import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.HistoricalDownload;

public interface CandleEngine {

    CandlePersistenceResult process(
            HistoricalDownload download
    );

}