package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.yahoo.adapter;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Candle;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out.HistoricalMarketDataPort;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.yahoo.YahooChartClient;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.yahoo.YahooChartResponse;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.yahoo.mapper.YahooHistoricalMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class YahooHistoricalAdapter
        implements HistoricalMarketDataPort {

    private final YahooChartClient client;

    private final YahooHistoricalMapper mapper;

    @Override
    @Retry(name = "yahoo")
    @CircuitBreaker(name = "yahoo", fallbackMethod = "loadHistoryFallback")
    @RateLimiter(name = "yahoo")
    public List<Candle> loadHistory(

            Long instrumentId,

            String ticker,

            String interval,

            Instant from,

            Instant to

    ) {

        validateRequest(
                ticker,
                interval,
                from,
                to
        );

        long start = System.currentTimeMillis();

        log.info(
                "Downloading Yahoo history instrumentId={} ticker={} interval={} from={} to={}",
                instrumentId,
                ticker,
                interval,
                from,
                to
        );

        YahooChartResponse response =
                client.history(
                        ticker,
                        interval,
                        from.getEpochSecond(),
                        to.getEpochSecond()
                );

        if (response == null
                || response.chart() == null
                || response.chart().result() == null
                || response.chart().result().isEmpty()) {

            log.warn(
                    "Yahoo returned no historical data ticker={}",
                    ticker
            );

            return Collections.emptyList();
        }

        List<Candle> candles =
                mapper.toDomain(
                        instrumentId,
                        interval,
                        response
                );

        log.info(
                "Yahoo downloaded {} candles ticker={} elapsed={} ms",
                candles.size(),
                ticker,
                System.currentTimeMillis() - start
        );

        return candles;
    }

    /**
     * Fallback cuando Yahoo no responde o el Circuit Breaker está abierto.
     */
    private List<Candle> loadHistoryFallback(

            Long instrumentId,

            String ticker,

            String interval,

            Instant from,

            Instant to,

            Exception ex

    ) {

        log.error(
                "Yahoo unavailable ticker={} interval={} reason={}",
                ticker,
                interval,
                ex.getMessage()
        );

        return Collections.emptyList();
    }

    private void validateRequest(

            String ticker,

            String interval,

            Instant from,

            Instant to

    ) {

        if (ticker == null || ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker is required");
        }

        if (interval == null || interval.isBlank()) {
            throw new IllegalArgumentException("Interval is required");
        }

        if (from == null || to == null) {
            throw new IllegalArgumentException("Date range is required");
        }

        if (!from.isBefore(to)) {
            throw new IllegalArgumentException("Invalid historical range");
        }
    }

}