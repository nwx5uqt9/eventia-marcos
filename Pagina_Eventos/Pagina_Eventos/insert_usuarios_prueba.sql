-- Script para crear usuarios de prueba para el sistema de login
-- Asegúrate de que existan los roles primero

USE pagina_eventos;

-- Verificar que existan los roles
SELECT * FROM rol_usuario;

-- Insertar usuarios de prueba (ajusta según tu base de datos)

-- 1. Usuario ADMINISTRADOR (Rol ID = 1)
INSERT INTO usuario (nombre, apellidos, nombreusuario, email, password, dni, edad, sexo, telefono, direccion, id_rol_usuario)
VALUES
('Admin', 'Sistema', 'admin', 'admin@eventia.com', 'admin123', '12345678', 30, 'M', '999888777', 'Av. Principal 123', 1)
ON DUPLICATE KEY UPDATE id=id;

-- 2. Usuario ORGANIZADOR (Rol ID = 2)
INSERT INTO usuario (nombre, apellidos, nombreusuario, email, password, dni, edad, sexo, telefono, direccion, id_rol_usuario)
VALUES
('Carlos', 'Organizador', 'organizador', 'organizador@eventia.com', 'org123', '87654321', 35, 'M', '999777666', 'Calle Eventos 456', 2)
ON DUPLICATE KEY UPDATE id=id;

-- 3. Usuario CLIENTE (Rol ID = 3)
INSERT INTO usuario (nombre, apellidos, nombreusuario, email, password, dni, edad, sexo, telefono, direccion, id_rol_usuario)
VALUES
('Juan', 'Pérez', 'cliente', 'cliente@eventia.com', 'cli123', '11223344', 25, 'M', '999666555', 'Jr. Los Olivos 789', 3)
ON DUPLICATE KEY UPDATE id=id;

-- Usuarios adicionales de prueba
INSERT INTO usuario (nombre, apellidos, nombreusuario, email, password, dni, edad, sexo, telefono, direccion, id_rol_usuario)
VALUES
('María', 'García', 'maria', 'maria@gmail.com', '123456', '22334455', 28, 'F', '999555444', 'Av. Los Pinos 321', 3),
('Pedro', 'López', 'pedro', 'pedro@gmail.com', '123456', '33445566', 32, 'M', '999444333', 'Calle Las Flores 654', 3),
('Ana', 'Torres', 'ana', 'ana@eventia.com', '123456', '44556677', 27, 'F', '999333222', 'Jr. San Martín 987', 2)
ON DUPLICATE KEY UPDATE id=id;

-- Verificar usuarios insertados
SELECT
    u.id,
    u.nombre,
    u.apellidos,
    u.nombreusuario,
    u.email,
    r.rol as 'Rol',
    r.id as 'ID Rol'
FROM usuario u
INNER JOIN rol_usuario r ON u.id_rol_usuario = r.id
ORDER BY r.id, u.nombre;

-- Mostrar credenciales de acceso
SELECT
    '=== CREDENCIALES DE PRUEBA ===' as 'INFO';

SELECT
    'ADMINISTRADOR' as 'Tipo',
    'admin' as 'Usuario',
    'admin@eventia.com' as 'Email',
    'admin123' as 'Password',
    '/admin' as 'Redirige a'
UNION ALL
SELECT
    'ORGANIZADOR' as 'Tipo',
    'organizador' as 'Usuario',
    'organizador@eventia.com' as 'Email',
    'org123' as 'Password',
    '/control' as 'Redirige a'
UNION ALL
SELECT
    'CLIENTE' as 'Tipo',
    'cliente' as 'Usuario',
    'cliente@eventia.com' as 'Email',
    'cli123' as 'Password',
    '/client' as 'Redirige a';

