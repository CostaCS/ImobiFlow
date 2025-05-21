ALTER TABLE imovel ADD COLUMN usuario_id UUID;

UPDATE imovel
SET usuario_id = '6416fc81-cdb9-4a58-95d7-6f23ca4b54db';

ALTER TABLE imovel
ADD CONSTRAINT fk_imovel_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id);

ALTER TABLE imovel ALTER COLUMN usuario_id SET NOT NULL;
