ALTER TABLE cliente ADD COLUMN usuario_id UUID;

UPDATE cliente SET usuario_id = '6416fc81-cdb9-4a58-95d7-6f23ca4b54db';

ALTER TABLE cliente
ADD CONSTRAINT fk_cliente_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id);

ALTER TABLE cliente ALTER COLUMN usuario_id SET NOT NULL;
