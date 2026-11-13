package mx.com.ciat.cio.market.platform.marketloaderservice.domain.event;

import java.time.Instant;
import java.util.UUID;

public record InstrumentLoadRequestedEvent(

        UUID eventId,
        UUID correlationId,
        Instant eventDate,

        String symbol,
        String exchange,
        String interval,

        Instant from,
        Instant to

) {}