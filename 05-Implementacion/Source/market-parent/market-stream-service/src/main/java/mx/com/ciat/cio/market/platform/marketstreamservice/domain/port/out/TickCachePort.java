package mx.com.ciat.cio.market.platform.marketstreamservice.domain.port.out;

import mx.com.ciat.cio.market.platform.marketstreamservice.domain.model.TickData;

public interface TickCachePort {
    void saveLatestPrice(TickData tick);
}