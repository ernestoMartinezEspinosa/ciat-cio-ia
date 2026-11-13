package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.error;

import org.springframework.context.annotation.Bean;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorHandlerConfig {

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(template);

        return new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3));
    }
    @Bean
    public DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<Object, Object> template) {

        DeadLetterPublishingRecoverer recoverer =
            new DeadLetterPublishingRecoverer(template,
                (record, ex) -> new TopicPartition(record.topic() + "-dlq", record.partition())
            );

        return new DefaultErrorHandler(recoverer, new FixedBackOff(0L, 0));
    }
}