package mx.com.ciat.cio.market.platform.marketstreamservice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketstreamservice.domain.model.TickData;
import mx.com.ciat.cio.market.platform.marketstreamservice.domain.port.in.ProcessTickUseCase;
import mx.com.ciat.cio.market.platform.marketstreamservice.domain.port.out.TickCachePort;
import mx.com.ciat.cio.market.platform.marketstreamservice.domain.port.out.TickPersistencePort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessTickService implements ProcessTickUseCase {

    private final TickCachePort cachePort;
    private final TickPersistencePort persistencePort;

    @Override
    public void process(TickData tick) {
        try {
            // 1. Actualización ultrarrápida en Redis (para clientes en vivo)
            cachePort.saveLatestPrice(tick);

            // 2. Persistencia en MySQL (UPSERT de la vela de 1 minuto)
            persistencePort.saveTick(tick);
            
        } catch (Exception e) {
            log.error("Error procesando tick para {}: {}", tick.symbol(), e.getMessage(), e);
        }
    }
}