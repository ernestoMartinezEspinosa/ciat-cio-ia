-- =====================================================================
-- DOCUMENTACIÓN DEL MODELO DE DATOS - CIAT CIO IA
-- Base de datos: marketdb
-- Propósito: Añadir comentarios descriptivos a cada tabla
-- =====================================================================

USE marketdb;

-- =====================================================================
-- 1. INGESTA Y PERSISTENCIA DE MERCADO
-- =====================================================================

ALTER TABLE candles 
    COMMENT = 'Almacena las velas OHLCV (Open, High, Low, Close, Volume) en múltiples temporalidades (1m, 5m, 1d). Es la fuente principal para el análisis técnico y backtesting.';

ALTER TABLE pricing 
    COMMENT = 'Registro histórico de precios (ticks) en tiempo real. Almacena el último precio conocido por instrumento y su timestamp.';

ALTER TABLE ingestion_control 
    COMMENT = 'Control de estado para los procesos de ingesta (WebSocket y REST). Registra la última ejecución, estado y errores para garantizar la idempotencia.';

ALTER TABLE processed_events 
    COMMENT = 'Registro de eventos procesados (idempotencia). Evita procesar dos veces el mismo evento (Kafka) o tick (WebSocket).';

-- =====================================================================
-- 2. CATÁLOGOS MAESTROS (INSTRUMENTOS Y SECTORES)
-- =====================================================================

ALTER TABLE instrument 
    COMMENT = 'Catálogo maestro de instrumentos financieros (acciones, ETFs, cripto). Contiene ticker, nombre, exchange, market_cap y la relación con el sector (sector_id).';

ALTER TABLE symbol 
    COMMENT = 'Catálogo alternativo de símbolos. Se usa para mapear tickers de diferentes proveedores (Yahoo, Alpaca, etc.) a un identificador interno.';

ALTER TABLE sector_strength 
    COMMENT = 'Catálogo de fuerza relativa por sector. Se actualiza diariamente y se usa en el Score (Cap. 15) para ponderar la fuerza del sector de un instrumento.';

ALTER TABLE market_session 
    COMMENT = 'Define los horarios de operación (Pre-market, Regular, After-hours) por exchange. Se usa para filtrar ticks fuera de horario.';

ALTER TABLE watchlist_universe 
    COMMENT = 'Universo de instrumentos monitoreados. Define qué tickers se suscriben al WebSocket y se evalúan en el motor de alertas.';

-- =====================================================================
-- 3. EVENTOS Y CATALIZADORES (FUNDAMENTALES)
-- =====================================================================

ALTER TABLE news_catalyst 
    COMMENT = 'Registro de noticias y catalizadores (FDA, contratos, earnings). Alimenta el criterio de Catalizador (Cap. 14) en el Score.';

ALTER TABLE earnings_event 
    COMMENT = 'Calendario de reportes de resultados (earnings). Se usa para evitar operar cerca de eventos de alta volatilidad.';

ALTER TABLE gap_event 
    COMMENT = 'Registro de Gaps (saltos de precio) al inicio de la sesión. Indica desequilibrios de oferta/demanda y posibles oportunidades.';

ALTER TABLE insider_transaction 
    COMMENT = 'Transacciones de insiders (directivos y accionistas mayoritarios). Se usa como señal de confianza o alerta fundamental.';

ALTER TABLE institutional_activity 
    COMMENT = 'Actividad de inversores institucionales (13F, flujos). Indica si el "dinero inteligente" está entrando o saliendo.';

-- =====================================================================
-- 4. INDICADORES TÉCNICOS Y CUANTITATIVOS
-- =====================================================================

ALTER TABLE technical_indicator 
    COMMENT = 'Almacena indicadores técnicos calculados (EMA, RSI, MACD, ATR) por instrumento y temporalidad. Se usa para confirmar señales.';

ALTER TABLE indicator_snapshot 
    COMMENT = 'Snapshot diario de indicadores clave. Permite un acceso rápido a los valores actuales sin recalcular.';

ALTER TABLE relative_strength 
    COMMENT = 'Fuerza relativa (RS) de un instrumento frente a su sector o índice. Identifica líderes y rezagados.';

ALTER TABLE market_breadth 
    COMMENT = 'Amplitud de mercado (avances/descensos, nuevos máximos/mínimos). Mide la salud general del mercado.';

ALTER TABLE option_flow 
    COMMENT = 'Flujo de opciones (volumen, open interest, puts/calls). Detecta apuestas institucionales y sentimiento.';

ALTER TABLE short_interest 
    COMMENT = 'Interés corto (short interest) y días para cubrir. Identifica posibles "short squeezes".';

-- =====================================================================
-- 5. SEÑALES, RIESGO Y EJECUCIÓN (CORE DEL NEGOCIO)
-- =====================================================================

ALTER TABLE trade_signal 
    COMMENT = 'Registro de señales generadas por el motor de alertas (market-alert-service). Incluye score, stop_loss, target1R, target2R y justificación.';

ALTER TABLE trade_order 
    COMMENT = 'Órdenes enviadas al broker (market, limit, stop). Registra estado, precio de ejecución y comisiones.';

ALTER TABLE trade_result 
    COMMENT = 'Resultado de las operaciones cerradas. Almacena el R obtenido (Cap. 9) y el P&L para calcular métricas de desempeño.';

ALTER TABLE cio_score 
    COMMENT = 'Score histórico calculado por el motor (0-100). Permite auditar por qué se tomó una decisión en un momento dado.';

ALTER TABLE cio_score_history 
    COMMENT = 'Historial de cambios de score por instrumento. Útil para backtesting y análisis de tendencias del score.';

-- =====================================================================
-- 6. AUDITORÍA Y CONTROL
-- =====================================================================

ALTER TABLE flyway_schema_history 
    COMMENT = 'Historial de migraciones de base de datos gestionadas por Flyway. Asegura la consistencia del esquema.';