ALTER TABLE negociacao
ADD COLUMN usuario_id UUID;

ALTER TABLE negociacao
ADD CONSTRAINT fk_negociacao_usuario
FOREIGN KEY (usuario_id) REFERENCES usuario(id);
