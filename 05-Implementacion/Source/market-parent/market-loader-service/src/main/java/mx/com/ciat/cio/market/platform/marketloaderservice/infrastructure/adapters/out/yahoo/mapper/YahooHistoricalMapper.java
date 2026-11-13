package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.yahoo.mapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Candle;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.yahoo.ChartResult;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.yahoo.Quote;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.yahoo.YahooChartResponse;

@Slf4j
@Component
public class YahooHistoricalMapper {

    public List<Candle> toDomain(
            Long instrumentId,
            String timeframe,
            YahooChartResponse response) {

        List<Candle> candles = new ArrayList<>();

        if (response == null
                || response.chart() == null
                || response.chart().result() == null
                || response.chart().result().isEmpty()) {

            log.warn("Yahoo returned empty historical response");
            return candles;
        }

        ChartResult result = response.chart().result().getFirst();

        if (result.indicators() == null
                || result.indicators().quote() == null
                || result.indicators().quote().isEmpty()) {

            log.warn("Yahoo response does not contain quote information");
            return candles;
        }

        Quote quote = result.indicators().quote().getFirst();

        int size = result.timestamp().size();

        for (int i = 0; i < size; i++) {

            if (quote.open().size() <= i
                    || quote.high().size() <= i
                    || quote.low().size() <= i
                    || quote.close().size() <= i
                    || quote.volume().size() <= i) {
                continue;
            }

            if (quote.open().get(i) == null
                    || quote.high().get(i) == null
                    || quote.low().get(i) == null
                    || quote.close().get(i) == null) {
                continue;
            }

            Instant openTime =
                    Instant.ofEpochSecond(result.timestamp().get(i));

            Instant closeTime =
                    calculateCloseTime(openTime, timeframe);

            Long volume =
                    quote.volume().get(i) == null
                            ? 0L
                            : quote.volume().get(i);

            candles.add(

                    new Candle(

                            null,

                            instrumentId,

                            timeframe,

                            openTime,

                            closeTime,

                            BigDecimal.valueOf(quote.open().get(i)),

                            BigDecimal.valueOf(quote.high().get(i)),

                            BigDecimal.valueOf(quote.low().get(i)),

                            BigDecimal.valueOf(quote.close().get(i)),

                            volume,

                            0,

                            Instant.now()

                    )

            );
        }

        log.info("Mapped {} historical candles", candles.size());

        return candles;
    }

    private Instant calculateCloseTime(
            Instant openTime,
            String timeframe) {

        return switch (timeframe) {

            case "1m" -> openTime.plusSeconds(60);

            case "2m" -> openTime.plusSeconds(120);

            case "5m" -> openTime.plusSeconds(300);

            case "15m" -> openTime.plusSeconds(900);

            case "30m" -> openTime.plusSeconds(1800);

            case "60m", "1h" -> openTime.plusSeconds(3600);

            case "90m" -> openTime.plusSeconds(5400);

            case "1d" -> openTime.plusSeconds(86400);

            case "5d" -> openTime.plusSeconds(432000);

            case "1wk" -> openTime.plusSeconds(604800);

            case "1mo" -> openTime.plusSeconds(2629746);

            case "3mo" -> openTime.plusSeconds(7889238);

            default -> openTime;
        };
    }

}