CREATE EXTENSION IF NOT EXISTS "pgcrypto";

ALTER TABLE imovel
RENAME COLUMN dataCadastro TO data_cadastro;