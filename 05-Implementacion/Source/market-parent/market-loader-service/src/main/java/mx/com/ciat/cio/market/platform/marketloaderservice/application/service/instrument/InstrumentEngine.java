package mx.com.ciat.cio.market.platform.marketloaderservice.application.service.instrument;

import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.HistoricalDownload;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Instrument;

public interface InstrumentEngine {

    Instrument getOrCreate(
            String ticker,
            String exchange
    );

    Instrument updateHistoricalLoad(
            Instrument instrument,
            HistoricalDownload download
    );

}