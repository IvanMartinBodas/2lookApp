-- ===== TABLAS PRINCIPALES =====

-- 1. Tabla Persona
CREATE TABLE IF NOT EXISTS persona (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    contraseña VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT NOW()
);

-- 2. Tabla Barbero
CREATE TABLE IF NOT EXISTS barbero (
    id SERIAL PRIMARY KEY,
    persona_id INTEGER NOT NULL UNIQUE REFERENCES persona(id) ON DELETE CASCADE,
    especialidad VARCHAR(100),
    foto_url VARCHAR(500),
    valoracion DECIMAL(2,1) DEFAULT 0.0,
    biografia VARCHAR(500)
);

-- 3. Tabla Servicio
CREATE TABLE IF NOT EXISTS servicio (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    precio DECIMAL(10,2) NOT NULL,
    duracion_min INTEGER NOT NULL
);

-- 4. Tabla Reserva
CREATE TABLE IF NOT EXISTS reserva (
    id SERIAL PRIMARY KEY,
    persona_id INTEGER NOT NULL REFERENCES persona(id),
    barbero_id INTEGER NOT NULL REFERENCES barbero(id),
    servicio_id INTEGER NOT NULL REFERENCES servicio(id),
    fecha_reserva TIMESTAMP NOT NULL,
    estado VARCHAR(20) DEFAULT 'PENDIENTE',
    precio_total DECIMAL(10,2)
);

-- 5. Tabla Favorito
CREATE TABLE IF NOT EXISTS favorito (
    id SERIAL PRIMARY KEY,
    persona_id INTEGER NOT NULL REFERENCES persona(id),
    barbero_id INTEGER NOT NULL REFERENCES barbero(id),
    fecha_agregado TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_favorito_unique UNIQUE(persona_id, barbero_id)
);

-- 6. Tabla Opinion
CREATE TABLE IF NOT EXISTS opinion (
    id SERIAL PRIMARY KEY,
    persona_id INTEGER NOT NULL REFERENCES persona(id),
    barbero_id INTEGER NOT NULL REFERENCES barbero(id),
    calificacion DECIMAL(2,1) NOT NULL,
    comentario VARCHAR(500),
    fecha_opinion TIMESTAMP DEFAULT NOW()
);

-- 7. Tabla HistorialDeCortes
CREATE TABLE IF NOT EXISTS historial_de_cortes (
    id SERIAL PRIMARY KEY,
    persona_id INTEGER NOT NULL REFERENCES persona(id),
    barbero_id INTEGER NOT NULL REFERENCES barbero(id),
    fecha_corte TIMESTAMP DEFAULT NOW(),
    descripcion VARCHAR(500),
    foto_url VARCHAR(500)
);