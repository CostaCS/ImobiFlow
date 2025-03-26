CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE usuario (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          nome VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          senha VARCHAR(255) NOT NULL,
                          telefone VARCHAR(20),
                          data_cadastro DATE DEFAULT CURRENT_TIMESTAMP
);