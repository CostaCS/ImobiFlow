CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE imobiliaria (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          nome VARCHAR(50) NOT NULL,
                          cnpj VARCHAR(20) NOT NULL UNIQUE,
                          email VARCHAR(50) NOT NULL UNIQUE,
                          telefone VARCHAR(20),
                          endereco VARCHAR (50)
);