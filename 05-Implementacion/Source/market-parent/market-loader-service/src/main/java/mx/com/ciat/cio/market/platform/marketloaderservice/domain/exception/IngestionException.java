package mx.com.ciat.cio.market.platform.marketloaderservice.domain.exception;

public class IngestionException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final IngestionErrorCode code;

    public IngestionException(
            IngestionErrorCode code,
            String message
    ) {
        super(message);
        this.code = code;
    }

    public IngestionException(
            IngestionErrorCode code,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.code = code;
    }

    public IngestionErrorCode getCode() {
        return code;
    }
}