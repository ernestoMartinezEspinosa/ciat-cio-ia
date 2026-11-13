package mx.com.ciat.cio.market.platform.marketloaderservice.application.service.download;

import java.time.Instant;

import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.HistoricalDownload;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Instrument;

public interface HistoricalDownloadEngine {

    HistoricalDownload download(

            Instrument instrument,

            String timeframe,

            Instant from,

            Instant to

    );

}