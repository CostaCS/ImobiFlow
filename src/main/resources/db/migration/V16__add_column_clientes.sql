ALTER TABLE cliente
ADD COLUMN imobiliaria_id UUID,
ADD CONSTRAINT fk_cliente_imobiliaria
  FOREIGN KEY (imobiliaria_id)
  REFERENCES imobiliaria(id);