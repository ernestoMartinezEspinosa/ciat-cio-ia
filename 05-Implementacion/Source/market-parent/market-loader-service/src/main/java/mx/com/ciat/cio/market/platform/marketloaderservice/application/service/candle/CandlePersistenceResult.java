package mx.com.ciat.cio.market.platform.marketloaderservice.application.service.candle;

public record CandlePersistenceResult(

        long downloaded,

        long persisted

) {

    public static CandlePersistenceResult empty() {
        return new CandlePersistenceResult(0, 0);
    }

}