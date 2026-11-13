package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.yahoo;

import java.util.List;

public record Quote(

        List<Double> open,

        List<Double> high,

        List<Double> low,

        List<Double> close,

        List<Long> volume

) {
}