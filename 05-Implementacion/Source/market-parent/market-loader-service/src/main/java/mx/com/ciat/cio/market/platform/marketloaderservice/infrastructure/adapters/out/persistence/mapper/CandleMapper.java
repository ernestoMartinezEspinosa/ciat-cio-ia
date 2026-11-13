package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.mapper;

import org.springframework.stereotype.Component;

import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Candle;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity.CandleEntity;

@Component
public class CandleMapper {

    public CandleEntity toEntity(Candle candle) {

        if (candle == null) {
            return null;
        }

        CandleEntity entity = new CandleEntity();

        entity.setInstrumentId(candle.instrumentId());
        entity.setTimeframe(candle.timeframe());
        entity.setOpenTime(candle.openTime());
        entity.setCloseTime(candle.closeTime());

        entity.setOpenPrice(candle.openPrice());
        entity.setHighPrice(candle.highPrice());
        entity.setLowPrice(candle.lowPrice());
        entity.setClosePrice(candle.closePrice());

        entity.setVolume(candle.volume());
        entity.setTradesCount(candle.tradesCount());

        return entity;
    }

    public Candle toDomain(CandleEntity entity) {

        if (entity == null) {
            return null;
        }

        return new Candle(

                entity.getId(),

                entity.getInstrumentId(),

                entity.getTimeframe(),

                entity.getOpenTime(),

                entity.getCloseTime(),

                entity.getOpenPrice(),

                entity.getHighPrice(),

                entity.getLowPrice(),

                entity.getClosePrice(),

                entity.getVolume(),

                entity.getTradesCount(),

                entity.getCreatedAt()

        );
    }

}