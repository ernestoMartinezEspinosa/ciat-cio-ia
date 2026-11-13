package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.adapter;

import lombok.RequiredArgsConstructor;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Instrument;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out.InstrumentRepositoryPort;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.mapper.InstrumentMapper;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.InstrumentJpaRepository;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity.InstrumentEntity;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InstrumentJpaAdapter
        implements InstrumentRepositoryPort {

    private final InstrumentJpaRepository repository;
    private final InstrumentMapper mapper;

    @Override
    public Optional<Instrument> findByTicker(String ticker) {

        return repository
                .findByTicker(ticker)
                .map(mapper::toDomain);
    }

    @Override
    public Instrument save(Instrument instrument) {

        InstrumentEntity entity =
                mapper.toEntity(instrument);

        return mapper.toDomain(
                repository.save(entity)
        );
    }

    @Override
    public Optional<Instrument> findByTickerAndExchange(
            String ticker,
            String exchange
    ) {

        return repository
                .findByTickerAndExchangeName(ticker, exchange)
                .map(mapper::toDomain);

    }

}