package mx.com.ciat.cio.market.platform.marketloaderservice.infrastructure.adapters.out.yahoo;

public record Meta(

        String symbol,

        String exchangeName,

        String currency,

        String timezone

) {
}