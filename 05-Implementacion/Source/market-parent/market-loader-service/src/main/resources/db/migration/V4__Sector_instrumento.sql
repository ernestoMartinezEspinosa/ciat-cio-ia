-- 1. Aseguramos que la columna 'sector' en sector_strength sea única (necesario para la FK)
ALTER TABLE sector_strength 
  ADD UNIQUE KEY uk_sector (sector);

-- 2. Agregamos la columna sector_id con el mismo tipo que el id de sector_strength
ALTER TABLE instrument 
  ADD COLUMN sector_id BIGINT NULL AFTER exchange_name;

-- 3. Actualizamos los datos existentes para llenar el sector_id basándonos en el nombre del sector
UPDATE instrument i
JOIN sector_strength ss ON i.sector = ss.sector
SET i.sector_id = ss.id;

-- 4. Creamos la llave foránea
ALTER TABLE instrument
  ADD CONSTRAINT fk_instrument_sector 
  FOREIGN KEY (sector_id) REFERENCES sector_strength(id)
  ON DELETE SET NULL ON UPDATE CASCADE;

-- 5. Finalmente, eliminamos la columna 'sector' de tipo string
ALTER TABLE instrument 
  DROP COLUMN sector;