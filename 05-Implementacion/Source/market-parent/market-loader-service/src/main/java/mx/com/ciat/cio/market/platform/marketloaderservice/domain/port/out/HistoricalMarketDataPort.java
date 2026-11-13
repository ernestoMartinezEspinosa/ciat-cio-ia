package mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out;

import java.time.Instant;
import java.util.List;

import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Candle;

public interface HistoricalMarketDataPort {

    List<Candle> loadHistory(

            Long instrumentId,

            String ticker,

            String interval,

            Instant from,

            Instant to

    );

}