package mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out;

import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Instrument;

import java.util.Optional;

public interface InstrumentRepositoryPort {

    Optional<Instrument> findByTicker(String ticker);

    Optional<Instrument> findByTickerAndExchange(
            String ticker,
            String exchange
    );

    Instrument save(Instrument instrument);

}