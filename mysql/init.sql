-- Bases de datos para cada microservicio
CREATE DATABASE IF NOT EXISTS mascotasdb;
CREATE DATABASE IF NOT EXISTS reportesdb;
CREATE DATABASE IF NOT EXISTS usuariosdb;

-- Permisos al usuario
GRANT ALL PRIVILEGES ON mascotasdb.* TO 'sanosysalvos'@'%';
GRANT ALL PRIVILEGES ON reportesdb.* TO 'sanosysalvos'@'%';
GRANT ALL PRIVILEGES ON usuariosdb.* TO 'sanosysalvos'@'%';
FLUSH PRIVILEGES;