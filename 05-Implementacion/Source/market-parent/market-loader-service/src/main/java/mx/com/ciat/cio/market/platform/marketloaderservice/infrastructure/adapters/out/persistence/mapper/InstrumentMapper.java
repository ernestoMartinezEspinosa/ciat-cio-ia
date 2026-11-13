package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.mapper;

import org.springframework.stereotype.Component;

import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Instrument;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity.InstrumentEntity;
import mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.persistence.repository.entity.SectorStrengthEntity;

@Component
public class InstrumentMapper {

    public Instrument toDomain(InstrumentEntity entity) {
        if (entity == null) {
            return null;
        }

        // Extraemos los datos del sector si existe
        Long sectorId = entity.getSector() != null ? entity.getSector().getId() : null;
        String sectorName = entity.getSector() != null ? entity.getSector().getSector() : null;

        return new Instrument(
                entity.getId(),
                entity.getTicker(),
                entity.getCompanyName(),
                entity.getExchangeName(),
                sectorId,      // 👈 Mapeado desde la relación
                sectorName,    // 👈 Mapeado desde la relación
                entity.getIndustry(),
                entity.getMarketCap(),
                entity.getCurrency(),
                entity.getTimezone(),
                entity.isActive(),
                entity.getLastHistoricalLoad(),
                entity.getCreatedAt()
        );
    }

    public InstrumentEntity toEntity(Instrument instrument) {
        if (instrument == null) {
            return null;
        }

        InstrumentEntity entity = new InstrumentEntity();
        entity.setId(instrument.id());
        entity.setTicker(instrument.ticker());
        entity.setCompanyName(instrument.companyName());
        entity.setExchangeName(instrument.exchangeName());
        entity.setIndustry(instrument.industry());
        entity.setMarketCap(instrument.marketCap());
        entity.setCurrency(instrument.currency());
        entity.setTimezone(instrument.timezone());
        entity.setActive(instrument.active());
        entity.setLastHistoricalLoad(instrument.lastHistoricalLoad());
        
        // 👇 Si tienes el ID, creamos una referencia (proxy) para no ir a la BD
        if (instrument.sectorId() != null) {
            SectorStrengthEntity sectorRef = new SectorStrengthEntity();
            sectorRef.setId(instrument.sectorId());
            entity.setSector(sectorRef);
        }

        return entity;
    }

    public void updateEntity(Instrument instrument, InstrumentEntity entity) {
        entity.setTicker(instrument.ticker());
        entity.setCompanyName(instrument.companyName());
        entity.setExchangeName(instrument.exchangeName());
        entity.setIndustry(instrument.industry());
        entity.setMarketCap(instrument.marketCap());
        entity.setCurrency(instrument.currency());
        entity.setTimezone(instrument.timezone());
        entity.setActive(instrument.active());
        entity.setLastHistoricalLoad(instrument.lastHistoricalLoad());

        // 👇 Si actualizamos el sector, asignamos la referencia
        if (instrument.sectorId() != null) {
            SectorStrengthEntity sectorRef = new SectorStrengthEntity();
            sectorRef.setId(instrument.sectorId());
            entity.setSector(sectorRef);
        } else {
            entity.setSector(null);
        }
    }
}