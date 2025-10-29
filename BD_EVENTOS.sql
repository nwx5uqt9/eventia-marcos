

DROP TABLE boletas, cuentas, entradas, eventos, pagos;


CREATE TABLE cuentas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(20) NOT NULL,
    contraseña VARCHAR(20) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    fecha_nacimiento DATE,
    celular INT,
    correo CHAR(40),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rol INT
) ENGINE=InnoDB;

CREATE TABLE eventos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    lugar VARCHAR(50) NOT NULL,
    fecha TIMESTAMP NOT NULL,
    capacidad INT NOT NULL,
    descripcion VARCHAR(100),
    precio DOUBLE NOT NULL,
    organizador_id INT NOT NULL,
    FOREIGN KEY (organizador_id) REFERENCES cuentas(id)
) ENGINE=InnoDB;

CREATE TABLE pagos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    metodo_pago CHAR(20) NOT NULL,
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES cuentas(id)
) ENGINE=InnoDB;

CREATE TABLE boleta (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    eventos_id INT NOT NULL,
    pagos_id INT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES cuentas(id),
    FOREIGN KEY (eventos_id) REFERENCES eventos(id),
    FOREIGN KEY (pagos_id) REFERENCES pagos(id)
) ENGINE=InnoDB;

CREATE TABLE entrada (
    id INT PRIMARY KEY AUTO_INCREMENT,
    qro_codigo INT NOT NULL,
    asistencia INT,
    pago_id INT,
    FOREIGN KEY (pago_id) REFERENCES pagos(id)
) ENGINE=InnoDB;
