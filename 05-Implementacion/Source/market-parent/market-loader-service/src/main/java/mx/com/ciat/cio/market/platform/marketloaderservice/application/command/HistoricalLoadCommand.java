package mx.com.ciat.cio.market.platform.marketloaderservice.application.command;

import java.time.Instant;

public record HistoricalLoadCommand(

        Long instrumentId,

        String ticker,

        String exchange,

        String interval,

        Instant from,

        Instant to

) {
}