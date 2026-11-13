-- Asignar sectores faltantes según el tipo de activo
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Tecnología') WHERE ticker = 'TSLA';
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Tecnología') WHERE ticker = 'HYPER';
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Tecnología') WHERE ticker = 'HYPE';
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Tecnología') WHERE ticker = 'BRAI';
UPDATE instrument SET sector_id = (SELECT id FROM sector_strength WHERE sector = 'Tecnología') WHERE ticker = 'NBIL';

-- Verificar que ya no haya nulos
SELECT i.ticker, i.sector_id, ss.sector, ss.strength_score
FROM instrument i
LEFT JOIN sector_strength ss ON i.sector_id = ss.id
WHERE i.active = 1;