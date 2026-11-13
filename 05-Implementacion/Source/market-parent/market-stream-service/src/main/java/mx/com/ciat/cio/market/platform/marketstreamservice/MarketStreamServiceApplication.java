package mx.com.ciat.cio.market.platform.marketstreamservice;

import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketstreamservice.infrastructure.adapters.in.yahoo.YahooWebSocketClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class MarketStreamServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketStreamServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner startWebSocket(YahooWebSocketClient client) {
        return args -> {
            log.info("Spring Boot ha arrancado. Iniciando cliente WebSocket de Yahoo...");
            // Pequeña pausa de cortesía para asegurar que todos los beans (DB, Redis) estén listos
            Thread.sleep(2000); 
            client.connect();
        };
    }
}