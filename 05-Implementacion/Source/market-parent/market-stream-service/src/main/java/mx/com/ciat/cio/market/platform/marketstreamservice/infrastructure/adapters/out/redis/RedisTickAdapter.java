package mx.com.ciat.cio.market.platform.marketstreamservice.infrastructure.adapters.out.redis;

import lombok.RequiredArgsConstructor;
import mx.com.ciat.cio.market.platform.marketstreamservice.domain.model.TickData;
import mx.com.ciat.cio.market.platform.marketstreamservice.domain.port.out.TickCachePort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RedisTickAdapter implements TickCachePort {

    private final StringRedisTemplate redisTemplate;
    private static final String REDIS_KEY = "market:latest";

    @Override
    public void saveLatestPrice(TickData tick) {
        // Guardamos un JSON simple o un string con el formato que prefieras
        String value = String.format(
                "{\"symbol\":\"%s\",\"price\":%.4f,\"volume\":%d,\"ts\":%d,\"updatedAt\":\"%s\"}",
                tick.symbol(),
                tick.price(),
                tick.volume(),
                tick.timestampMs(),
                Instant.now().toString()
        );

        // HSET market:latest <symbol> <json>
        redisTemplate.opsForHash().put(REDIS_KEY, tick.symbol(), value);
    }
}