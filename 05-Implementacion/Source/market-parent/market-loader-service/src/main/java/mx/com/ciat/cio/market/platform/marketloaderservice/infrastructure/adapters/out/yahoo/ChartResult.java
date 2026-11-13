package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.yahoo;

import java.util.List;

public record ChartResult(

        Meta meta,

        List<Long> timestamp,

        Indicators indicators

) {
}