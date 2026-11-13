package mx.com.ciat.cio.market.platform.marketloaderservice.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Candle(

        Long id,

        Long instrumentId,

        String timeframe,

        Instant openTime,

        Instant closeTime,

        BigDecimal openPrice,

        BigDecimal highPrice,

        BigDecimal lowPrice,

        BigDecimal closePrice,

        Long volume,

        Integer tradesCount,

        Instant createdAt

) {

    public static Candle create(

            Long instrumentId,

            String timeframe,

            Instant openTime,

            Instant closeTime,

            BigDecimal openPrice,

            BigDecimal highPrice,

            BigDecimal lowPrice,

            BigDecimal closePrice,

            Long volume

    ) {

        validatePrices(
                openPrice,
                highPrice,
                lowPrice,
                closePrice
        );

        return new Candle(

                null,

                instrumentId,

                timeframe,

                openTime,

                closeTime,

                openPrice,

                highPrice,

                lowPrice,

                closePrice,

                volume == null ? 0L : volume,

                0,

                Instant.now()

        );

    }

    private static void validatePrices(

            BigDecimal open,

            BigDecimal high,

            BigDecimal low,

            BigDecimal close

    ) {

        if (high.compareTo(low) < 0) {
            throw new IllegalArgumentException(
                    "High price cannot be lower than Low price");
        }

        if (open.compareTo(low) < 0 || open.compareTo(high) > 0) {
            throw new IllegalArgumentException(
                    "Open price is outside candle range");
        }

        if (close.compareTo(low) < 0 || close.compareTo(high) > 0) {
            throw new IllegalArgumentException(
                    "Close price is outside candle range");
        }

    }

	public Long id() {
		return id;
	}

	public Long instrumentId() {
		return instrumentId;
	}

	public String timeframe() {
		return timeframe;
	}

	public Instant openTime() {
		return openTime;
	}

	public Instant closeTime() {
		return closeTime;
	}

	public BigDecimal openPrice() {
		return openPrice;
	}

	public BigDecimal highPrice() {
		return highPrice;
	}

	public BigDecimal lowPrice() {
		return lowPrice;
	}

	public BigDecimal closePrice() {
		return closePrice;
	}

	public Long volume() {
		return volume;
	}

	public Integer tradesCount() {
		return tradesCount;
	}

	public Instant createdAt() {
		return createdAt;
	}

}