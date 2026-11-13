ALTER TABLE ingestion_control
    CHANGE COLUMN processed_records downloaded_records BIGINT NULL;

ALTER TABLE ingestion_control
    ADD COLUMN persisted_records BIGINT NULL AFTER downloaded_records,
    ADD COLUMN execution_time_ms BIGINT NULL AFTER execution_end,
    ADD COLUMN instrument_id BIGINT NULL,
    ADD COLUMN requested_from TIMESTAMP NULL,
    ADD COLUMN requested_to TIMESTAMP NULL,
    ADD COLUMN ticker VARCHAR(50) NULL,
    ADD COLUMN timeframe VARCHAR(20) NULL;