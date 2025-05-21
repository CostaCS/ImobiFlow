CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE imovel (
    id UUID PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    endereco VARCHAR(255),
    tipoImovel VARCHAR(100),
    metragem DOUBLE PRECISION,
    preco DOUBLE PRECISION,
    status VARCHAR(100),
    id_imobiliaria UUID NOT NULL,
    dataCadastro DATE,

    CONSTRAINT fk_imobiliaria_imovel
        FOREIGN KEY (id_imobiliaria)
        REFERENCES imobiliaria(id)
        ON DELETE CASCADE
);

CREATE TABLE negociacao (
    id UUID PRIMARY KEY,
    id_imovel UUID NOT NULL,
    id_imobiliaria UUID NOT NULL,
    id_cliente UUID NOT NULL,
    contatoCliente VARCHAR(255),
    valorProposto DOUBLE PRECISION,
    status VARCHAR(100),
    dataNegociacao DATE,

    CONSTRAINT fk_negociacao_imovel
        FOREIGN KEY (id_imovel)
        REFERENCES imovel(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_negociacao_imobiliaria
        FOREIGN KEY (id_imobiliaria)
        REFERENCES imobiliaria(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_negociacao_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente(id)
        ON DELETE CASCADE
);