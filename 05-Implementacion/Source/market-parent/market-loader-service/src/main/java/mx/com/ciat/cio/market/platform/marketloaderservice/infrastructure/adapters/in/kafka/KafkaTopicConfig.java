package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.in.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    private final String topic;


    public KafkaTopicConfig(
            @Value("${spring.kafka.topics.instrument-load-requested}") String topic
    ) {
        this.topic = topic;
    }


    @Bean
    public NewTopic instrumentLoadRequestedTopic() {

        return TopicBuilder
                .name(topic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}