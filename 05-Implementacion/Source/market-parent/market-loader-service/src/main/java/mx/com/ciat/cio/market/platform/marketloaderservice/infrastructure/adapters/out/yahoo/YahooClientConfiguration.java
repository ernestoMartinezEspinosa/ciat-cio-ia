package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.yahoo;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class YahooClientConfiguration {

    @Bean
    YahooChartClient yahooChartClient(

            @Value("${yahoo.finance.base-url}")
            String baseUrl,

            @Value("${yahoo.finance.connect-timeout}")
            Duration connectTimeout,

            @Value("${yahoo.finance.read-timeout}")
            Duration readTimeout

    ) {

        HttpClient httpClient =
                HttpClient.newBuilder()
                        .connectTimeout(connectTimeout)
                        .build();

        JdkClientHttpRequestFactory requestFactory =
                new JdkClientHttpRequestFactory(httpClient);

        requestFactory.setReadTimeout(readTimeout);

        RestClient restClient =
                RestClient.builder()
                        .baseUrl(baseUrl)
                        .requestFactory(requestFactory)

                        .defaultHeader(
                                "User-Agent",
                                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/138.0 Safari/537.36")

                        .defaultHeader(
                                "Accept",
                                "application/json")

                        .defaultHeader(
                                "Accept-Language",
                                "en-US,en;q=0.9")

                        .build();

        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory
                        .builderFor(RestClientAdapter.create(restClient))
                        .build();

        return factory.createClient(YahooChartClient.class);

    }

}