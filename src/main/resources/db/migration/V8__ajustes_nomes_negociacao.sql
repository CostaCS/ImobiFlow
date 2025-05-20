CREATE EXTENSION IF NOT EXISTS "pgcrypto";

ALTER TABLE negociacao
RENAME COLUMN contatoCliente TO contato_cliente;

ALTER TABLE negociacao
RENAME COLUMN valorProposto TO valor_proposto;

ALTER TABLE negociacao
RENAME COLUMN dataNegociacao TO data_negociacao;