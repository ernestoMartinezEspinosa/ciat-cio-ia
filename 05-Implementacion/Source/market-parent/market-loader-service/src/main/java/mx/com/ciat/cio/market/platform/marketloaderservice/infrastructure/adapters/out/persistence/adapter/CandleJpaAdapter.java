package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.adapter;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Candle;
import mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out.CandleRepositoryPort;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.mapper.CandleMapper;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.CandleJpaRepository;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity.CandleEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
@Transactional
public class CandleJpaAdapter implements CandleRepositoryPort {


    private final CandleJpaRepository repository;

    private final CandleMapper mapper;


    @Override
    public void saveAll(List<Candle> candles) {


        List<CandleEntity> entities =
                candles.stream()
                        .map(mapper::toEntity)
                        .toList();


        repository.saveAll(entities);
    }


	@Override
	public Candle save(Candle candle) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Optional<Candle> findById(UUID id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}


	@Override
	public List<Candle> findByInstrumentIdAndOpenTimeBetween(UUID instrumentId, Instant from, Instant to) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void deleteById(UUID id) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean existsById(UUID id) {
		// TODO Auto-generated method stub
		return false;
	}
}