CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE cliente (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          nome VARCHAR(50) NOT NULL,
                          email VARCHAR(50) NOT NULL UNIQUE,
                          telefone VARCHAR(20),
                          endereco VARCHAR (50)
);