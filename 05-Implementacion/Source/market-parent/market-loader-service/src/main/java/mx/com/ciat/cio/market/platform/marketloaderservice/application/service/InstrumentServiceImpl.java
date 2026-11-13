package mx.com.ciat.cio.market.platform.marketloaderservice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.HistoricalDownload;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Instrument;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out.InstrumentRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentRepositoryPort repository;

    @Override
    public Instrument getOrCreate(
            String ticker,
            String exchange
    ) {

        return repository
                .findByTicker(ticker)
                .orElseGet(() -> createInstrument(ticker, exchange));
    }

    private Instrument createInstrument(
            String ticker,
            String exchange
    ) {

        log.info(
                "Creating instrument ticker={} exchange={}",
                ticker,
                exchange
        );

        return repository.save(
                Instrument.create(
                        ticker,
                        exchange
                )
        );
    }

    @Override
    public Instrument save(
            Instrument instrument
    ) {
        return repository.save(instrument);
    }

    @Override
    public Instrument update(
            Instrument instrument
    ) {
        return repository.save(instrument);
    }

    @Override
    public Instrument markHistoricalLoadCompleted(
            Instrument instrument,
            HistoricalDownload download
    ) {

        if (download == null || download.isEmpty()) {
            return instrument;
        }

        instrument.withLastHistoricalLoad(
                download.lastHistoricalLoad()
        );

        Instrument updated =
                repository.save(instrument);

        log.info(
                "Historical load updated instrumentId={} ticker={} lastHistoricalLoad={}",
                updated.id(),
                updated.ticker(),
                updated.lastHistoricalLoad()
        );

        return updated;
    }

}