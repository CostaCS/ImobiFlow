ALTER TABLE imobiliaria ADD COLUMN usuario_id UUID;

UPDATE imobiliaria SET usuario_id = '6416fc81-cdb9-4a58-95d7-6f23ca4b54db';

ALTER TABLE imobiliaria
ADD CONSTRAINT fk_imobiliaria_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id);

ALTER TABLE imobiliaria ALTER COLUMN usuario_id SET NOT NULL;