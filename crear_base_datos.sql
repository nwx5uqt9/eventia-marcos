-- Script para crear la base de datos y verificar la conexión
-- Ejecutar este script ANTES de iniciar Spring Boot

-- Eliminar la base de datos si existe (CUIDADO: Borra todos los datos)
-- DROP DATABASE IF EXISTS pagina_eventos;

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS pagina_eventos
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE pagina_eventos;

-- Verificar que la base de datos esté vacía o mostrar tablas existentes
SHOW TABLES;

-- Verificar la conexión
SELECT 'Conexión exitosa a pagina_eventos' AS status;

