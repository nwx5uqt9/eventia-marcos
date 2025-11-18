-- ========================================
-- Script para corregir el campo sexo y Flyway
-- IMPORTANTE: Ejecuta este script ANTES de iniciar la aplicación
-- ========================================

USE pagina_eventos;

-- Verificar la estructura actual
DESCRIBE usuario;

-- Modificar el campo sexo para aumentar su longitud
ALTER TABLE usuario MODIFY COLUMN sexo VARCHAR(20);

-- Limpiar el registro de Flyway de la migración V2 (que ya no existe)
DELETE FROM flyway_schema_history WHERE version = '2';

-- Verificar el cambio
DESCRIBE usuario;

-- Verificar el historial de Flyway
SELECT * FROM flyway_schema_history;

-- Mensaje de confirmación
SELECT 'Campo sexo actualizado correctamente a VARCHAR(20)' AS Estado;
SELECT 'Historial de Flyway limpiado' AS Estado;

