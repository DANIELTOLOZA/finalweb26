-- Limpieza de tablas para evitar errores de duplicidad en reinicios
DELETE FROM pokemon.combate;
DELETE FROM pokemon.captura;
DELETE FROM pokemon.entrenador;
DELETE FROM pokemon.pokemon;
DELETE FROM pokemon.tipo_pokemon;
DELETE FROM pokemon.pueblo;

-- Insertar Pueblos
INSERT INTO pokemon.pueblo (id, nombre, uuid) VALUES (1, 'Pueblo Paleta', 'c71b059e-4c12-4f30-80be-b51f0ab9a8a7');
INSERT INTO pokemon.pueblo (id, nombre, uuid) VALUES (2, 'Ciudad Verde', 'd8fe3761-fa5d-45db-9c3f-4e0ab8ef2eb7');
INSERT INTO pokemon.pueblo (id, nombre, uuid) VALUES (3, 'Ciudad Celeste', 'f76d90bb-8e3b-419b-a010-cb8352b2f6ef');
INSERT INTO pokemon.pueblo (id, nombre, uuid) VALUES (4, 'Ciudad Carmín', 'a3ef2c1b-2521-4f6c-9418-8094d1f2b234');

-- Insertar Tipos de Pokemon
INSERT INTO pokemon.tipo_pokemon (id, descripcion, uuid) VALUES (1, 'Fuego', '0be2d323-5e92-4f81-80a1-63cb5df5a3c0');
INSERT INTO pokemon.tipo_pokemon (id, descripcion, uuid) VALUES (2, 'Agua', '449410fb-5ef3-40f4-a82f-2d7c0f1883be');
INSERT INTO pokemon.tipo_pokemon (id, descripcion, uuid) VALUES (3, 'Planta', 'b18d2ff8-86d7-48f8-8bb2-53b5fa773e35');
INSERT INTO pokemon.tipo_pokemon (id, descripcion, uuid) VALUES (4, 'Eléctrico', 'ea53d712-4c28-4e89-9a7c-502a3a5f8ef8');
INSERT INTO pokemon.tipo_pokemon (id, descripcion, uuid) VALUES (5, 'Psíquico', '7ad0ff13-25e2-411a-be19-86ab0d6ee6ab');

-- Insertar Pokemon
INSERT INTO pokemon.pokemon (id, nombre, descripcion, tipo_pokemon, fecha_descubrimiento, generacion, uuid) 
VALUES (1, 'Bulbasaur', 'Este Pokémon nace con una semilla en el lomo, que brota con el paso del tiempo.', 3, '1996-02-27', 1, '57b28014-a957-41ec-b8d4-fdf696ab9cf2');
INSERT INTO pokemon.pokemon (id, nombre, descripcion, tipo_pokemon, fecha_descubrimiento, generacion, uuid) 
VALUES (2, 'Charmander', 'Prefiere las cosas calientes. Dicen que cuando llueve le sale vapor de la punta de la cola.', 1, '1996-02-27', 1, 'a60eb224-ec40-424a-b5e1-8ee22dbbf6cb');
INSERT INTO pokemon.pokemon (id, nombre, descripcion, tipo_pokemon, fecha_descubrimiento, generacion, uuid) 
VALUES (3, 'Squirtle', 'El caparazón de Squirtle no sirve únicamente para protegerlo. Su forma redondeada y sus hendiduras reducen la resistencia al agua.', 2, '1996-02-27', 1, '4ffc6bb5-5d93-4a11-b0db-6e69bb0d5db6');
INSERT INTO pokemon.pokemon (id, nombre, descripcion, tipo_pokemon, fecha_descubrimiento, generacion, uuid) 
VALUES (4, 'Pikachu', 'Tiene unas bolsas en las mejillas donde almacena electricidad. Cuando la libera de golpe, la potencia es similar a la de un rayo.', 4, '1996-02-27', 1, 'e377f012-70b5-4b20-bdfa-b1db9d2b86ab');

-- Insertar Entrenadores
INSERT INTO pokemon.entrenador (id, nombre, apellido, fecha_nacimiento, fecha_vinculacion, pueblo_id, uuid)
VALUES (1, 'Ash', 'Ketchum', '1987-05-22', '1997-04-01', 1, '7f9bb8c2-3e2c-49f3-8ea8-cf2bb22a84ac');
INSERT INTO pokemon.entrenador (id, nombre, apellido, fecha_nacimiento, fecha_vinculacion, pueblo_id, uuid)
VALUES (2, 'Misty', 'Waterflower', '1988-06-12', '1998-05-15', 3, '87cf9bb5-4f3c-41c3-be99-2e21bbdb83fb');
INSERT INTO pokemon.entrenador (id, nombre, apellido, fecha_nacimiento, fecha_vinculacion, pueblo_id, uuid)
VALUES (3, 'Brock', 'Harrison', '1985-08-01', '1998-05-15', 2, '9a2bc8e2-c4bc-467a-8f3a-c8e11a2f1b40');

-- Insertar Capturas
INSERT INTO pokemon.captura (pokemon_id, entrenador_id) VALUES (4, 1); -- Ash caputró Pikachu
INSERT INTO pokemon.captura (pokemon_id, entrenador_id) VALUES (2, 1); -- Ash capturó Charmander
INSERT INTO pokemon.captura (pokemon_id, entrenador_id) VALUES (3, 2); -- Misty capturó Squirtle

-- Insertar Combates
INSERT INTO pokemon.combate (id, fecha_combate, resultado, entrenador_id, uuid)
VALUES (1, '2026-06-18', 'Ganado', 1, '83d0c4fb-4b2e-4b68-8de1-8bf88ad2281a');
INSERT INTO pokemon.combate (id, fecha_combate, resultado, entrenador_id, uuid)
VALUES (2, '2026-06-19', 'Perdido', 2, '24f1c50e-5a1e-45fa-bb29-9e8a38a7c2be');

