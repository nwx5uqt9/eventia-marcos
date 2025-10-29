-- Actualización del campo sexo en la tabla Usuario
-- De: CHAR(1) a VARCHAR(10)
-- Razón: Permitir valores completos como "Masculino" o "Femenino" en lugar de solo 'M' o 'F'
-- Fecha: 2025-10-28

-- Para MySQL
ALTER TABLE usuario MODIFY COLUMN sexo VARCHAR(10);

-- Si necesitas actualizar datos existentes de 'M'/'F' a valores completos:
-- UPDATE usuario SET sexo = 'Masculino' WHERE sexo = 'M';
-- UPDATE usuario SET sexo = 'Femenino' WHERE sexo = 'F';

-- Para PostgreSQL (si cambias de base de datos en el futuro)
-- ALTER TABLE usuario ALTER COLUMN sexo TYPE VARCHAR(10);

