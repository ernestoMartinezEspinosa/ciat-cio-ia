package mx.com.ciat.cio.market.platform.marketloaderservice.application.service.instrument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.service.InstrumentService;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.HistoricalDownload;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Instrument;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InstrumentEngineImpl implements InstrumentEngine {

    private final InstrumentService instrumentService;

    @Override
    public Instrument getOrCreate(
            String ticker,
            String exchange
    ) {

        return instrumentService.getOrCreate(
                ticker,
                exchange
        );

    }

    @Override
    public Instrument updateHistoricalLoad(
            Instrument instrument,
            HistoricalDownload download
    ) {

        return instrumentService.markHistoricalLoadCompleted(
                instrument,
                download
        );

    }

}