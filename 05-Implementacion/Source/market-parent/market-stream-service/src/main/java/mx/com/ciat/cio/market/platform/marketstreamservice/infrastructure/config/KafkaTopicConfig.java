package mx.com.ciat.cio.market.platform.marketstreamservice.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic marketTicksTopic() {
        return TopicBuilder.name("market-ticks")
                .partitions(3)    // Para escalar horizontalmente
                .replicas(1)      // En producción, usar 3
                .build();
    }

    // Opcional: Crear también el tópico de señales para la Fase 2
    @Bean
    public NewTopic marketSignalsTopic() {
        return TopicBuilder.name("market-signals")
                .partitions(3)
                .replicas(1)
                .build();
    }
}