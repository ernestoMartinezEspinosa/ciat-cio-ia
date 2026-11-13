package mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.in;

import mx.com.ciat.cio.market.platform.marketloaderservice.domain.event.InstrumentLoadRequestedEvent;

/**
 * Caso de uso para procesar la solicitud de carga de instrumentos.
 */
public interface InstrumentLoadRequestedUseCase {

    /**
     * Ejecuta el procesamiento del evento de carga de instrumentos.
     *
     * @param event evento recibido desde Kafka con la solicitud de carga
     */
    void execute(InstrumentLoadRequestedEvent event);
}