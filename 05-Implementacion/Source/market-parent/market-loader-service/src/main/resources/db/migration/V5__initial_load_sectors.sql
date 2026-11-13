-- 1. Limpiar la tabla (opcional, para evitar duplicados)
-- TRUNCATE TABLE sector_strength;

-- 2. Insertar los sectores estratégicos (alineados a tu Manual PRO+ Cap. 2 y 14)
INSERT INTO sector_strength (sector, strength_score, score_date) VALUES
('IA y Semiconductores', 0.85, CURDATE()),  -- Sector líder (Cap. 2 y 14)
('Defensa y Drones', 0.80, CURDATE()),      -- Sector prioritario (Cap. 2)
('Biotech', 0.70, CURDATE()),               -- Sector de crecimiento (Cap. 2)
('Energía', 0.60, CURDATE()),               -- Sector cíclico (Cap. 2)
('Oro', 0.50, CURDATE()),                   -- Refugio seguro (Cap. 2)
('Tecnología', 0.75, CURDATE()),            -- Sector amplio
('Financiero', 0.55, CURDATE()),            -- Sector neutro
('Salud', 0.65, CURDATE()),                 -- Sector defensivo
('Consumo', 0.45, CURDATE()),               -- Sector neutro
('Industrial', 0.40, CURDATE());            -- Sector débil

-- 3. Asignar sectores a los instrumentos existentes (basado en el ticker)
-- Primero, actualizamos por ticker. Ajusta según tus instrumentos reales.

UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'IA y Semiconductores') WHERE ticker IN ('AMD', 'NVDA', 'INTC', 'TSM');
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Defensa y Drones') WHERE ticker IN ('BE', 'PDSB', 'LMT', 'RTX');
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Biotech') WHERE ticker IN ('MRNA', 'PFE', 'JNJ');
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Energía') WHERE ticker IN ('XOM', 'CVX');
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Oro') WHERE ticker IN ('GOLD', 'NEM');
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Tecnología') WHERE ticker IN ('AAPL', 'MSFT', 'GOOGL', 'AMZN');
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Financiero') WHERE ticker IN ('JPM', 'BAC');
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Salud') WHERE ticker IN ('UNH', 'CVS');
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Consumo') WHERE ticker IN ('WMT', 'COST');
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Industrial') WHERE ticker IN ('CAT', 'DE');

-- 4. (Opcional) Si tienes tickers como SDEV o HYPE32196-USD, asígnales un sector manualmente
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Tecnología') WHERE ticker = 'SDEV';
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Tecnología') WHERE ticker = 'HYPE32196-USD';

-- 5. Verificar que se asignaron correctamente
SELECT i.ticker, i.sector_id, ss.sector, ss.strength_score
FROM instrument i
LEFT JOIN sector_strength ss ON i.sector_id = ss.id
WHERE i.active = 1;