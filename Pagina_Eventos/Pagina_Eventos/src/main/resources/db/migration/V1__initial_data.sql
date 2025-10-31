INSERT INTO rol_usuario (rol, descripcion) VALUES
('administrador', 'Administrador del sistema con acceso completo'),
('organizador', 'Organizador de eventos'),
('cliente', 'Cliente que compra boletos');

INSERT INTO tipo_evento (nombre, descripcion) VALUES
('Concierto', 'Evento musical en vivo'),
('Conferencia', 'Evento educativo o corporativo'),
('Festival', 'Festival con múltiples actividades'),
('Show', 'Espectáculo de entretenimiento'),
('Deportivo', 'Evento deportivo');

INSERT INTO estado_evento (nombre, descripcion) VALUES
('Disponible', 'Evento con boletos disponibles'),
('Próximo', 'Evento próximo a realizarse'),
('Lleno', 'Evento sin boletos disponibles'),
('Finalizado', 'Evento ya realizado'),
('Cancelado', 'Evento cancelado');

INSERT INTO organizador (nombre_empresa, descripcion, telefono) VALUES
('EventMaster Inc', 'Organizadora líder de eventos corporativos', '+1234567890'),
('Live Productions', 'Especialistas en conciertos y shows', '+0987654321'),
('Sports Events Pro', 'Organizadores de eventos deportivos', '+1122334455');

INSERT INTO ubicacion (nombre, descripcion, capacidad) VALUES
('Estadio Nacional', 'Gran estadio para eventos masivos', 50000),
('Centro de Convenciones', 'Espacio para conferencias y ferias', 5000),
('Teatro Principal', 'Teatro para espectáculos culturales', 2000),
('Auditorio Municipal', 'Auditorio para eventos diversos', 1500);

