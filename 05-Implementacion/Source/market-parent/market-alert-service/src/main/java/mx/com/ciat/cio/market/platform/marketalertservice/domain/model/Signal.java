package mx.com.ciat.cio.market.platform.marketalertservice.domain.model;


import java.math.BigDecimal;

import java.time.Instant;


public record Signal(

        String symbol,

        String type, // "BUY", "SELL", "WATCH"

        BigDecimal price,

        Instant timestamp,

        int score,

        BigDecimal stopLoss,

        BigDecimal target1R,

        BigDecimal target2R,

        String reason

) {}
