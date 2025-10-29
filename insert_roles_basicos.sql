-- Script para insertar roles básicos en la tabla rol_usuario
-- Fecha: 2025-10-28
-- Propósito: Asegurar que existan los roles necesarios para el sistema

USE pagina_eventos;

-- Limpiar tabla si existe (CUIDADO: Solo en desarrollo)
-- DELETE FROM rol_usuario;

-- Insertar roles básicos del sistema
INSERT INTO rol_usuario (id, rol, descripcion) VALUES
(1, 'Administrador', 'Acceso completo al sistema'),
(2, 'Usuario', 'Usuario estándar del sistema'),
(3, 'Cliente', 'Cliente que compra boletos')
ON DUPLICATE KEY UPDATE
    rol = VALUES(rol),
    descripcion = VALUES(descripcion);

-- Verificar que se insertaron correctamente
SELECT * FROM rol_usuario;

