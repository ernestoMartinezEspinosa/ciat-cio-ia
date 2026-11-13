package mx.com.ciat.cio.market.platform.marketloaderservice.domain.model;

import java.time.Instant;
import java.util.List;

public record HistoricalDownload(

        Instrument instrument,

        List<Candle> candles,

        Instant firstCandle,

        Instant lastCandle,

        int downloadedRecords

) {

    public boolean isEmpty() {
        return candles == null || candles.isEmpty();
    }

    public Instant lastHistoricalLoad() {
        return lastCandle;
    }

}