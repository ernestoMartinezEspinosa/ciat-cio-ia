package mx.com.ciat.cio.market.platform.marketloaderservice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketloaderservice.application.service.candle.CandlePersistenceResult;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Candle;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out.CandleRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CandleService {

    private final CandleRepositoryPort candleRepositoryPort;

    public CandlePersistenceResult process(
            List<Candle> candles
    ) {

        if (candles == null || candles.isEmpty()) {

            log.info("No candles to persist.");

            return CandlePersistenceResult.empty();
        }

        candleRepositoryPort.saveAll(candles);

        log.info(
                "Persisted {} candles.",
                candles.size()
        );

        return new CandlePersistenceResult(
                candles.size(),
                candles.size()
        );

    }

}