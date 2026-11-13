package mx.com.ciat.cio.market.platform.marketloaderservice.application.service.download;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Candle;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.HistoricalDownload;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Instrument;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out.HistoricalMarketDataPort;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoricalDownloadEngineImpl
        implements HistoricalDownloadEngine {

    private final HistoricalMarketDataPort historicalMarketDataPort;

    @Override
    public HistoricalDownload download(

            Instrument instrument,

            String timeframe,

            Instant from,

            Instant to

    ) {

        log.info(
                "Downloading historical data ticker={} timeframe={}",
                instrument.ticker(),
                timeframe
        );

        List<Candle> candles =
                historicalMarketDataPort.loadHistory(

                        instrument.id(),

                        instrument.ticker(),

                        timeframe,

                        from,

                        to

                );

        if (candles.isEmpty()) {

            return new HistoricalDownload(
                    instrument,
                    candles,
                    null,
                    null,
                    0
            );
        }

        Instant first =
                candles.stream()
                        .map(Candle::openTime)
                        .min(Comparator.naturalOrder())
                        .orElse(null);

        Instant last =
                candles.stream()
                        .map(Candle::openTime)
                        .max(Comparator.naturalOrder())
                        .orElse(null);

        log.info(
                "Downloaded {} candles ticker={}",
                candles.size(),
                instrument.ticker()
        );

        return new HistoricalDownload(

                instrument,

                candles,

                first,

                last,

                candles.size()

        );

    }

}