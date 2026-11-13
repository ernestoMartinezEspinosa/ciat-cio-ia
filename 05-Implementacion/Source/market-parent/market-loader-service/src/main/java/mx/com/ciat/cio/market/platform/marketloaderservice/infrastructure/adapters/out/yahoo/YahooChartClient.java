package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.yahoo;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface YahooChartClient {

    /**
     * Yahoo Finance Chart API
     *
     * Ejemplo:
     *
     * GET
     * https://query1.finance.yahoo.com/v8/finance/chart/AAPL
     *      ?interval=1d
     *      &period1=1704067200
     *      &period2=1719792000
     */
    @GetExchange("/v8/finance/chart/{symbol}")
    YahooChartResponse history(

            @PathVariable("symbol")
            String symbol,

            @RequestParam("interval")
            String interval,

            @RequestParam("period1")
            long from,

            @RequestParam("period2")
            long to

    );

}