package mx.com.ciat.cio.market.platform.marketstreamservice.domain.port.out;

import mx.com.ciat.cio.market.platform.marketstreamservice.domain.model.TickData;

public interface TickPersistencePort {
    void saveTick(TickData tick);
}