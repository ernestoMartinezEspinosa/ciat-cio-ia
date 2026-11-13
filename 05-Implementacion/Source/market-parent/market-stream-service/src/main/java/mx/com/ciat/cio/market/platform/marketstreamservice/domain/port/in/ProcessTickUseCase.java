package mx.com.ciat.cio.market.platform.marketstreamservice.domain.port.in;

import mx.com.ciat.cio.market.platform.marketstreamservice.domain.model.TickData;

public interface ProcessTickUseCase {
    void process(TickData tick);
}