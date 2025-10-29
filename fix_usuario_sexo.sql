-- Script de corrección rápida para el error de deserialización de sexo
-- Ejecutar directamente en MySQL si es necesario

USE pagina_eventos;

-- Actualizar el tipo de columna
ALTER TABLE usuario MODIFY COLUMN sexo VARCHAR(10);

-- Verificar el cambio
DESCRIBE usuario;

-- Nota: Con spring.jpa.hibernate.ddl-auto=update,
-- Hibernate debería hacer este cambio automáticamente al reiniciar la aplicación.

