CREATE EXTENSION IF NOT EXISTS "pgcrypto";

ALTER TABLE imovel
RENAME COLUMN tipoImovel TO tipo_imovel;