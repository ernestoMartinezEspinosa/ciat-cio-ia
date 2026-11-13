package mx.com.ciat.cio.market.platform.marketstreamservice.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketstreamservice.infrastructure.adapters.out.persistence.CatalogRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogCacheService {

    private final CatalogRepository repository;
    private final StringRedisTemplate redisTemplate;

    private static final String CACHE_KEY_PREFIX = "instrument:";

    @EventListener(ApplicationReadyEvent.class)
    public void loadCacheToRedisOnStartup() {
        log.info("🔄 [STARTUP] Iniciando carga del catálogo de instrumentos y sectores a Redis...");
        refreshCache();
    }

    public void refreshCache() {
        try {
            List<Object[]> results = repository.findInstrumentsWithSectorStrength();

            if (results.isEmpty()) {
                log.warn("⚠️ No se encontraron instrumentos activos para cargar en Redis.");
                return;
            }

            int count = 0;
            for (Object[] row : results) {
                try {
                    String ticker = (String) row[0];
                    String sector = row[1] != null ? (String) row[1] : "UNKNOWN";
                    double strength = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;

                    String key = CACHE_KEY_PREFIX + ticker;
                    
                    redisTemplate.opsForHash().put(key, "sector", sector);
                    redisTemplate.opsForHash().put(key, "strength", String.valueOf(strength));
                    
                    count++;
                } catch (Exception e) {
                    log.error("❌ Error procesando fila del catálogo: {}", e.getMessage());
                }
            }

            log.info("✅ Catálogo cargado exitosamente en Redis. Total instrumentos: {}", count);
            
        } catch (Exception e) {
            log.error("❌ Error crítico cargando el catálogo a Redis: {}", e.getMessage(), e);
        }
    }

    // 👇 MÉTODO QUE FALTABA
    public String getSector(String ticker) {
        Object sector = redisTemplate.opsForHash().get(CACHE_KEY_PREFIX + ticker, "sector");
        return sector != null ? sector.toString() : "UNKNOWN";
    }

    // 👇 MÉTODO QUE FALTABA
    public double getSectorStrength(String ticker) {
        Object strength = redisTemplate.opsForHash().get(CACHE_KEY_PREFIX + ticker, "strength");
        if (strength != null) {
            try {
                return Double.parseDouble(strength.toString());
            } catch (NumberFormatException e) {
                log.warn("⚠️ Valor de fuerza inválido para {}: {}", ticker, strength);
            }
        }
        return 0.0;
    }
}