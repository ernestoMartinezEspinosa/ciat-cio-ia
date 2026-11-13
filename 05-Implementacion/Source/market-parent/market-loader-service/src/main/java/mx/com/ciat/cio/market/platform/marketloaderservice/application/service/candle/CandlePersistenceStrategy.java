package mx.com.ciat.cio.market.platform.marketloaderservice.application.service.candle;

import java.util.List;

import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Candle;

public interface CandlePersistenceStrategy {

    CandlePersistenceResult process(
            List<Candle> candles
    );

}