-- =====================================================
-- CIAT-CIO INITIAL SCHEMA
-- Flyway V1
-- =====================================================
-- Schema base del microservicio

SET NAMES utf8mb4;

-- =====================================================
-- SYMBOL
-- =====================================================

CREATE TABLE symbol (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    ticker VARCHAR(20) NOT NULL,
    company_name VARCHAR(255),
    exchange_name VARCHAR(50),

    sector VARCHAR(100),
    industry VARCHAR(150),

    market_cap BIGINT,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    UNIQUE KEY uk_symbol_ticker (ticker)
);

-- =====================================================
-- WATCHLIST
-- =====================================================

CREATE TABLE watchlist_universe (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    enabled BOOLEAN NOT NULL DEFAULT TRUE,

    priority_level INT DEFAULT 1,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_watchlist_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- REAL TIME PRICING
-- =====================================================

CREATE TABLE pricing (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    price DECIMAL(18,6) NOT NULL,

    change_amount DECIMAL(18,6),

    change_percent DECIMAL(18,6),

    market_hours INT,

    quote_type INT,

    event_time TIMESTAMP(3) NOT NULL,

    received_at TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_pricing_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

CREATE INDEX idx_pricing_symbol_time
ON pricing(symbol_id,event_time);

-- =====================================================
-- CANDLES
-- =====================================================

CREATE TABLE candles (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    timeframe VARCHAR(10) NOT NULL,

    open_time TIMESTAMP NOT NULL,

    close_time TIMESTAMP NOT NULL,

    open_price DECIMAL(18,6) NOT NULL,

    high_price DECIMAL(18,6) NOT NULL,

    low_price DECIMAL(18,6) NOT NULL,

    close_price DECIMAL(18,6) NOT NULL,

    volume BIGINT DEFAULT 0,

    trades_count INT DEFAULT 0,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_candle_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id),

    UNIQUE KEY uk_candle (
        symbol_id,
        timeframe,
        open_time
    )
);

CREATE INDEX idx_candle_lookup
ON candles(symbol_id,timeframe,open_time);

-- =====================================================
-- TECHNICAL INDICATORS
-- =====================================================

CREATE TABLE technical_indicator (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    timeframe VARCHAR(10) NOT NULL,

    indicator_type VARCHAR(50) NOT NULL,

    period_value INT,

    value DECIMAL(18,8) NOT NULL,

    trade_signal VARCHAR(20),

    candle_time TIMESTAMP NOT NULL,

    calculated_at TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_technical_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

CREATE INDEX idx_technical_search
ON technical_indicator(
    symbol_id,
    timeframe,
    indicator_type,
    candle_time
);

-- =====================================================
-- SNAPSHOT INDICATORS
-- =====================================================

CREATE TABLE indicator_snapshot (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    timeframe VARCHAR(10) NOT NULL,

    rsi_14 DECIMAL(18,8),

    ema_9 DECIMAL(18,8),
    ema_21 DECIMAL(18,8),
    ema_50 DECIMAL(18,8),
    ema_200 DECIMAL(18,8),

    macd DECIMAL(18,8),
    macd_signal DECIMAL(18,8),

    atr_14 DECIMAL(18,8),

    adx_14 DECIMAL(18,8),

    stochastic_k DECIMAL(18,8),

    stochastic_d DECIMAL(18,8),

    vwap DECIMAL(18,8),

    snapshot_time TIMESTAMP NOT NULL,

    CONSTRAINT fk_snapshot_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- NEWS CATALYSTS
-- =====================================================

CREATE TABLE news_catalyst (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT,

    title VARCHAR(1000) NOT NULL,

    source_name VARCHAR(255),

    sentiment VARCHAR(20),

    published_at TIMESTAMP,

    article_url VARCHAR(2000),

    created_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_news_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- GENERIC CATALYST
-- =====================================================

CREATE TABLE catalyst (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    catalyst_type VARCHAR(50) NOT NULL,

    catalyst_date TIMESTAMP,

    impact_score DECIMAL(10,4),

    description VARCHAR(1000),

    created_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_catalyst_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- EARNINGS
-- =====================================================

CREATE TABLE earnings_event (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    earnings_date DATE NOT NULL,

    estimated_eps DECIMAL(18,6),

    reported_eps DECIMAL(18,6),

    surprise_percent DECIMAL(18,6),

    revenue_estimate BIGINT,

    revenue_actual BIGINT,

    created_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_earnings_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- INSIDER TRANSACTIONS
-- =====================================================

CREATE TABLE insider_transaction (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    insider_name VARCHAR(255),

    transaction_type VARCHAR(50),

    shares BIGINT,

    average_price DECIMAL(18,6),

    transaction_date DATE,

    created_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_insider_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- INSTITUTIONAL ACTIVITY
-- =====================================================

CREATE TABLE institutional_activity (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    institution_name VARCHAR(255),

    filing_date DATE,

    shares BIGINT,

    action_type VARCHAR(50),

    CONSTRAINT fk_inst_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- SHORT INTEREST
-- =====================================================

CREATE TABLE short_interest (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    report_date DATE NOT NULL,

    short_float_percent DECIMAL(18,6),

    short_ratio DECIMAL(18,6),

    shares_short BIGINT,

    CONSTRAINT fk_short_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- OPTION FLOW
-- =====================================================

CREATE TABLE option_flow (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    event_time TIMESTAMP NOT NULL,

    strike_price DECIMAL(18,6),

    expiration_date DATE,

    option_type VARCHAR(10),

    premium DECIMAL(18,6),

    contracts BIGINT,

    sentiment VARCHAR(20),

    CONSTRAINT fk_option_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- GAP EVENTS
-- =====================================================

CREATE TABLE gap_event (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    event_date DATE NOT NULL,

    previous_close DECIMAL(18,6),

    premarket_price DECIMAL(18,6),

    open_price DECIMAL(18,6),

    gap_percent DECIMAL(18,6),

    volume_ratio DECIMAL(18,6),

    direction VARCHAR(10),

    CONSTRAINT fk_gap_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- MARKET SESSION
-- =====================================================

CREATE TABLE market_session (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    trading_date DATE NOT NULL,

    premarket_high DECIMAL(18,6),
    premarket_low DECIMAL(18,6),

    regular_high DECIMAL(18,6),
    regular_low DECIMAL(18,6),

    afterhours_high DECIMAL(18,6),
    afterhours_low DECIMAL(18,6),

    CONSTRAINT fk_market_session_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- RELATIVE STRENGTH
-- =====================================================

CREATE TABLE relative_strength (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    rs_score DECIMAL(10,4),

    score_date DATE NOT NULL,

    CONSTRAINT fk_rs_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- SECTOR STRENGTH
-- =====================================================

CREATE TABLE sector_strength (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    sector VARCHAR(100),

    strength_score DECIMAL(10,4),

    score_date DATE NOT NULL
);

-- =====================================================
-- MARKET BREADTH
-- =====================================================

CREATE TABLE market_breadth (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    breadth_date DATE NOT NULL,

    advancing_issues INT,

    declining_issues INT,

    new_highs INT,

    new_lows INT,

    put_call_ratio DECIMAL(10,4),

    vix_value DECIMAL(10,4)
);

-- =====================================================
-- CIO SCORE
-- =====================================================

CREATE TABLE cio_score (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    score DECIMAL(10,4),

    technical_score DECIMAL(10,4),

    momentum_score DECIMAL(10,4),

    catalyst_score DECIMAL(10,4),

    volume_score DECIMAL(10,4),

    recommendation VARCHAR(20),

    calculated_at TIMESTAMP
        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_cio_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- SCORE HISTORY
-- =====================================================

CREATE TABLE ciat_cio_score_history (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    score_date TIMESTAMP NOT NULL,

    final_score DECIMAL(10,4),

    catalyst_score DECIMAL(10,4),

    momentum_score DECIMAL(10,4),

    volume_score DECIMAL(10,4),

    insider_score DECIMAL(10,4),

    earnings_score DECIMAL(10,4),

    rank_position INT,

    CONSTRAINT fk_history_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- TRADE SIGNAL
-- =====================================================

CREATE TABLE trade_signal (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    symbol_id BIGINT NOT NULL,

    signal_time TIMESTAMP NOT NULL,

    signal_type VARCHAR(20),

    entry_price DECIMAL(18,6),

    stop_loss DECIMAL(18,6),

    target_price DECIMAL(18,6),

    confidence_score DECIMAL(10,4),

    CONSTRAINT fk_signal_symbol
        FOREIGN KEY (symbol_id)
        REFERENCES symbol(id)
);

-- =====================================================
-- TRADE RESULT
-- =====================================================

CREATE TABLE trade_result (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    signal_id BIGINT NOT NULL,

    exit_time TIMESTAMP,

    exit_price DECIMAL(18,6),

    pnl_percent DECIMAL(18,6),

    result_status VARCHAR(20),

    CONSTRAINT fk_trade_result_signal
        FOREIGN KEY (signal_id)
        REFERENCES trade_signal(id)
);

-- =====================================================
-- INGESTION CONTROL
-- =====================================================

CREATE TABLE ingestion_control (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    source_name VARCHAR(100) NOT NULL,

    execution_start TIMESTAMP NOT NULL,

    execution_end TIMESTAMP,

    status VARCHAR(20),

    processed_records BIGINT,

    error_message TEXT
);

-- =====================================================
-- CIAT-CIO MARKET MIGRATION V2
-- symbol -> instrument migration
-- =====================================================

SET NAMES utf8mb4;

-- =====================================================
-- 1. CREATE NEW CORE TABLE: instrument
-- =====================================================

CREATE TABLE instrument (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    ticker VARCHAR(20) NOT NULL,
    company_name VARCHAR(255),
    exchange_name VARCHAR(50),

    sector VARCHAR(100),
    industry VARCHAR(150),

    market_cap BIGINT,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    UNIQUE KEY uk_instrument_ticker (ticker)
);

-- =====================================================
-- 2. MIGRATE DATA FROM symbol -> instrument
-- =====================================================

INSERT INTO instrument (
    id,
    ticker,
    company_name,
    exchange_name,
    sector,
    industry,
    market_cap,
    active,
    created_at
)
SELECT
    id,
    ticker,
    company_name,
    exchange_name,
    sector,
    industry,
    market_cap,
    active,
    created_at
FROM symbol;

-- =====================================================
-- 3. DISABLE OLD FOREIGN KEYS (SAFE DROP STEP)
-- =====================================================

-- Watchlist
ALTER TABLE watchlist_universe DROP FOREIGN KEY fk_watchlist_symbol;

-- Pricing
ALTER TABLE pricing DROP FOREIGN KEY fk_pricing_symbol;

-- Candles
ALTER TABLE candles DROP FOREIGN KEY fk_candle_symbol;

-- Technical indicator
ALTER TABLE technical_indicator DROP FOREIGN KEY fk_technical_symbol;

-- Snapshot
ALTER TABLE indicator_snapshot DROP FOREIGN KEY fk_snapshot_symbol;

-- News
ALTER TABLE news_catalyst DROP FOREIGN KEY fk_news_symbol;

-- Catalyst
ALTER TABLE catalyst DROP FOREIGN KEY fk_catalyst_symbol;

-- Earnings
ALTER TABLE earnings_event DROP FOREIGN KEY fk_earnings_symbol;

-- Insider
ALTER TABLE insider_transaction DROP FOREIGN KEY fk_insider_symbol;

-- Institutional
ALTER TABLE institutional_activity DROP FOREIGN KEY fk_inst_symbol;

-- Short interest
ALTER TABLE short_interest DROP FOREIGN KEY fk_short_symbol;

-- Option flow
ALTER TABLE option_flow DROP FOREIGN KEY fk_option_symbol;

-- Gap events
ALTER TABLE gap_event DROP FOREIGN KEY fk_gap_symbol;

-- Market session
ALTER TABLE market_session DROP FOREIGN KEY fk_market_session_symbol;

-- RS
ALTER TABLE relative_strength DROP FOREIGN KEY fk_rs_symbol;

-- CIO score
ALTER TABLE cio_score DROP FOREIGN KEY fk_cio_symbol;

-- History
ALTER TABLE ciat_cio_score_history DROP FOREIGN KEY fk_history_symbol;

-- Trade signal
ALTER TABLE trade_signal DROP FOREIGN KEY fk_signal_symbol;

-- Trade result (no direct FK to symbol)

-- =====================================================
-- 4. RENAME COLUMNS symbol_id -> instrument_id
-- =====================================================

ALTER TABLE watchlist_universe
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE pricing
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE candles
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE technical_indicator
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE indicator_snapshot
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE news_catalyst
    CHANGE symbol_id instrument_id BIGINT;

ALTER TABLE catalyst
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE earnings_event
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE insider_transaction
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE institutional_activity
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE short_interest
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE option_flow
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE gap_event
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE market_session
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE relative_strength
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE cio_score
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE ciat_cio_score_history
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

ALTER TABLE trade_signal
    CHANGE symbol_id instrument_id BIGINT NOT NULL;

-- =====================================================
-- 5. ADD NEW FOREIGN KEYS TO instrument
-- =====================================================

ALTER TABLE watchlist_universe
ADD CONSTRAINT fk_watchlist_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE pricing
ADD CONSTRAINT fk_pricing_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE candles
ADD CONSTRAINT fk_candle_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE technical_indicator
ADD CONSTRAINT fk_technical_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE indicator_snapshot
ADD CONSTRAINT fk_snapshot_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE news_catalyst
ADD CONSTRAINT fk_news_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE catalyst
ADD CONSTRAINT fk_catalyst_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE earnings_event
ADD CONSTRAINT fk_earnings_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE insider_transaction
ADD CONSTRAINT fk_insider_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE institutional_activity
ADD CONSTRAINT fk_inst_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE short_interest
ADD CONSTRAINT fk_short_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE option_flow
ADD CONSTRAINT fk_option_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE gap_event
ADD CONSTRAINT fk_gap_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE market_session
ADD CONSTRAINT fk_market_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE relative_strength
ADD CONSTRAINT fk_rs_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE cio_score
ADD CONSTRAINT fk_cio_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE ciat_cio_score_history
ADD CONSTRAINT fk_history_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

ALTER TABLE trade_signal
ADD CONSTRAINT fk_signal_instrument
FOREIGN KEY (instrument_id)
REFERENCES instrument(id);

-- =====================================================
-- 6. DROP OLD TABLE (OPTIONAL BUT RECOMMENDED AFTER VALIDATION)
-- =====================================================

-- SOLO habilitar cuando verifiques migración OK
-- DROP TABLE symbol;

-- =====================================================
-- END OF V2 MIGRATION
-- =====================================================

-- V3__create_instrument_table.sql


CREATE TABLE trade_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    instrument_id BIGINT NOT NULL,

    execution_type ENUM('FULL_UNIT', 'CASH_AMOUNT') NOT NULL,

    quantity DECIMAL(18,8),
    amount DECIMAL(18,8),
    currency VARCHAR(10),

    side VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,

    price DECIMAL(18,8),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    executed_at TIMESTAMP NULL,

    CONSTRAINT fk_trade_order_instrument
        FOREIGN KEY (instrument_id)
        REFERENCES instrument(id)
);
ALTER TABLE trade_order ADD CONSTRAINT chk_execution_logic
CHECK (
    (execution_type = 'FULL_UNIT' AND quantity IS NOT NULL AND amount IS NULL)
 OR (execution_type = 'CASH_AMOUNT' AND amount IS NOT NULL AND quantity IS NULL)
);

-- Script V4

ALTER TABLE instrument ADD COLUMN currency VARCHAR(10) DEFAULT 'USD', ADD COLUMN timezone VARCHAR(50) DEFAULT 'UTC';

INSERT INTO instrument (
    ticker,
    company_name,
    exchange_name,
    sector,
    industry,
    market_cap,
    active
) VALUES
('AAPL', 'Apple Inc.', 'NASDAQ', 'TECH', 'EQUITY', 3000000000000, 1),
('TSLA', 'Tesla Inc.', 'NASDAQ', 'AUTO', 'EQUITY', 800000000000, 1),
('NVDA', 'NVIDIA Corp.', 'NASDAQ', 'TECH', 'EQUITY', 2000000000000, 1),
('AMD', 'Advanced Micro Devices', 'NASDAQ', 'TECH', 'EQUITY', 250000000000, 1);

-- Script v5

CREATE TABLE processed_events (
    event_id CHAR(36) PRIMARY KEY,          -- UUID como texto
    correlation_id CHAR(36),                -- UUID como texto
    processed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_processed_events_correlation
ON processed_events (correlation_id);
