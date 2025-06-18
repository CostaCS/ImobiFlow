CREATE EXTENSION IF NOT EXISTS "pgcrypto";

ALTER TABLE imovel
ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE negociacao
ALTER COLUMN id SET DEFAULT gen_random_uuid();