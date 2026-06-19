CREATE SCHEMA IF NOT EXISTS pokemon;

CREATE TABLE IF NOT EXISTS pokemon.pueblo (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    uuid VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS pokemon.tipo_pokemon (
    id SERIAL PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL,
    uuid VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS pokemon.pokemon (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    tipo_pokemon INTEGER NOT NULL,
    fecha_descubrimiento DATE NOT NULL,
    generacion INTEGER NOT NULL,
    uuid VARCHAR(100) NOT NULL,
    FOREIGN KEY (tipo_pokemon) REFERENCES pokemon.tipo_pokemon(id)
);

CREATE TABLE IF NOT EXISTS pokemon.entrenador (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    fecha_vinculacion DATE NOT NULL,
    pueblo_id INTEGER NOT NULL,
    uuid VARCHAR(100) NOT NULL,
    FOREIGN KEY (pueblo_id) REFERENCES pokemon.pueblo(id)
);

CREATE TABLE IF NOT EXISTS pokemon.captura (
    pokemon_id INTEGER NOT NULL,
    entrenador_id INTEGER NOT NULL,
    PRIMARY KEY (pokemon_id, entrenador_id),
    FOREIGN KEY (pokemon_id) REFERENCES pokemon.pokemon(id) ON DELETE CASCADE,
    FOREIGN KEY (entrenador_id) REFERENCES pokemon.entrenador(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS pokemon.combate (
    id SERIAL PRIMARY KEY,
    fecha_combate DATE NOT NULL,
    resultado VARCHAR(50) NOT NULL,
    entrenador_id INTEGER NOT NULL,
    uuid VARCHAR(100) NOT NULL,
    FOREIGN KEY (entrenador_id) REFERENCES pokemon.entrenador(id) ON DELETE CASCADE
);

