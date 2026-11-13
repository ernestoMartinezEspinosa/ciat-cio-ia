ALTER TABLE instrument
ADD COLUMN last_historical_load TIMESTAMP NULL;

CREATE INDEX idx_instrument_last_load
ON instrument(last_historical_load);

CREATE INDEX idx_candle_incremental
ON candles (
    instrument_id,
    open_time DESC
);

CREATE INDEX idx_pricing_received
ON pricing(received_at);