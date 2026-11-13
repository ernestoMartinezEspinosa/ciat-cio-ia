package mx.com.ciat.cio.market.platform.marketloaderservice.domain.port.out;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import mx.com.ciat.cio.market.platform.marketloaderservice.domain.model.Candle;

public interface CandleRepositoryPort {

    /**
     * Guarda una vela de mercado
     *
     * @param candle candle de dominio
     * @return candle persistida
     */
    Candle save(Candle candle);


    /**
     * Busca una vela por identificador
     *
     * @param id identificador de candle
     * @return candle encontrada
     */
    Optional<Candle> findById(UUID id);


    /**
     * Consulta velas de un instrumento en un rango de tiempo
     *
     * @param instrumentId identificador del instrumento
     * @param from fecha inicio
     * @param to fecha fin
     * @return lista de candles
     */
    List<Candle> findByInstrumentIdAndOpenTimeBetween(
            UUID instrumentId,
            Instant from,
            Instant to
    );


    /**
     * Elimina una vela
     *
     * @param id identificador candle
     */
    void deleteById(UUID id);


    /**
     * Verifica existencia
     *
     * @param id identificador candle
     * @return true si existe
     */
    boolean existsById(UUID id);


	void saveAll(List<Candle> candles);

}