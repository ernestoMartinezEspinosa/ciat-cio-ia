package mx.com.ciat.cio.market.platform.marketloaderservice.application.service;

import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.HistoricalDownload;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Instrument;

public interface InstrumentService {

    Instrument getOrCreate(
            String ticker,
            String exchange
    );

    Instrument save(
            Instrument instrument
    );

    Instrument update(
            Instrument instrument
    );

    Instrument markHistoricalLoadCompleted(
            Instrument instrument,
            HistoricalDownload download
    );

}