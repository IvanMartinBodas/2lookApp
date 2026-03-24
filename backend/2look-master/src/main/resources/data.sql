-- ===== PERSONAS BARBEROS =====
INSERT INTO persona (nombre, email, password, rol, fecha_registro)
SELECT 'Charles Smith', 'charles@2look.com', '1234', 'BARBERO', NOW()
WHERE NOT EXISTS (SELECT 1 FROM persona WHERE email = 'charles@2look.com');

INSERT INTO persona (nombre, email, password, rol, fecha_registro)
SELECT 'Richard Jones', 'richard@2look.com', '1234', 'BARBERO', NOW()
WHERE NOT EXISTS (SELECT 1 FROM persona WHERE email = 'richard@2look.com');

INSERT INTO persona (nombre, email, password, rol, fecha_registro)
SELECT 'Marc Andrew', 'marc@2look.com', '1234', 'BARBERO', NOW()
WHERE NOT EXISTS (SELECT 1 FROM persona WHERE email = 'marc@2look.com');

-- ===== CLIENTES =====
INSERT INTO persona (nombre, email, password, rol, fecha_registro)
SELECT 'Carlos García', 'carlos@gmail.com', '1234', 'CLIENTE', NOW()
WHERE NOT EXISTS (SELECT 1 FROM persona WHERE email = 'carlos@gmail.com');

INSERT INTO persona (nombre, email, password, rol, fecha_registro)
SELECT 'Miguel Torres', 'miguel@gmail.com', '1234', 'CLIENTE', NOW()
WHERE NOT EXISTS (SELECT 1 FROM persona WHERE email = 'miguel@gmail.com');

INSERT INTO persona (nombre, email, password, rol, fecha_registro)
SELECT 'Alejandro Ruiz', 'alejandro@gmail.com', '1234', 'CLIENTE', NOW()
WHERE NOT EXISTS (SELECT 1 FROM persona WHERE email = 'alejandro@gmail.com');

INSERT INTO persona (nombre, email, password, rol, fecha_registro)
SELECT 'David Martínez', 'david@gmail.com', '1234', 'CLIENTE', NOW()
WHERE NOT EXISTS (SELECT 1 FROM persona WHERE email = 'david@gmail.com');

INSERT INTO persona (nombre, email, password, rol, fecha_registro)
SELECT 'Pablo Sánchez', 'pablo@gmail.com', '1234', 'CLIENTE', NOW()
WHERE NOT EXISTS (SELECT 1 FROM persona WHERE email = 'pablo@gmail.com');

-- ===== BARBEROS =====
INSERT INTO barbero (persona_id, especialidad, valoracion, biografia)
SELECT 1, 'Fade y degradados', 4.0, 'Especialista en cortes modernos con más de 5 años de experiencia'
WHERE NOT EXISTS (SELECT 1 FROM barbero WHERE persona_id = 1);

INSERT INTO barbero (persona_id, especialidad, valoracion, biografia)
SELECT 2, 'Cortes con tijera', 5.0, 'Maestro en cortes precisos con tijera'
WHERE NOT EXISTS (SELECT 1 FROM barbero WHERE persona_id = 2);

INSERT INTO barbero (persona_id, especialidad, valoracion, biografia)
SELECT 3, 'Cortes clásicos', 3.0, 'Experto en estilos vintage y cortes tradicionales'
WHERE NOT EXISTS (SELECT 1 FROM barbero WHERE persona_id = 3);

-- ===== SERVICIOS =====
INSERT INTO servicio (nombre, descripcion, precio, duracion_min)
SELECT 'Taper Fade', 'Degradado suave desde la nuca hacia arriba', 10.00, 30
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Taper Fade');

INSERT INTO servicio (nombre, descripcion, precio, duracion_min)
SELECT 'Burst Fade', 'Degradado en forma de arco alrededor de la oreja', 10.00, 30
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Burst Fade');

INSERT INTO servicio (nombre, descripcion, precio, duracion_min)
SELECT 'Drop Fade', 'Degradado que cae detrás de la oreja', 10.00, 30
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Drop Fade');

INSERT INTO servicio (nombre, descripcion, precio, duracion_min)
SELECT 'Corte Clásico', 'Corte tradicional con tijera y máquina', 10.00, 30
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Corte Clásico');

INSERT INTO servicio (nombre, descripcion, precio, duracion_min)
SELECT 'Corte Texturizado', 'Corte moderno con textura y volumen', 10.00, 30
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Corte Texturizado');

INSERT INTO servicio (nombre, descripcion, precio, duracion_min)
SELECT 'Corte Francés', 'Corte elegante estilo europeo', 10.00, 30
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Corte Francés');

INSERT INTO servicio (nombre, descripcion, precio, duracion_min)
SELECT 'Corte Infantil', 'Corte para niños hasta 12 años', 10.00, 25
WHERE NOT EXISTS (SELECT 1 FROM servicio WHERE nombre = 'Corte Infantil');