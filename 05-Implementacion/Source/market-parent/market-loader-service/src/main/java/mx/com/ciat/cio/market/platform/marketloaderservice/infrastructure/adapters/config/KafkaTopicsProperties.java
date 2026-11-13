package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka.topics")
public class KafkaTopicsProperties {

    private String instrumentLoadRequested;
    private String retry1;
    private String retry2;
    private String dlq;

    public String getInstrumentLoadRequested() {
        return instrumentLoadRequested;
    }

    public void setInstrumentLoadRequested(String instrumentLoadRequested) {
        this.instrumentLoadRequested = instrumentLoadRequested;
    }

    public String getRetry1() {
        return retry1;
    }

    public void setRetry1(String retry1) {
        this.retry1 = retry1;
    }

    public String getRetry2() {
        return retry2;
    }

    public void setRetry2(String retry2) {
        this.retry2 = retry2;
    }

    public String getDlq() {
        return dlq;
    }

    public void setDlq(String dlq) {
        this.dlq = dlq;
    }
}