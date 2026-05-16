# msvc-usuarios

Microservicio de gestión de usuarios. Puerto: **8083**

## Patrón implementado
**Adapter** — `MascotaClientAdapter` adapta las respuestas de msvc-mascotas al formato esperado por usuarios.

## Requisitos
- Java 17, Maven 3.8+
- Docker Desktop (MySQL y Keycloak via `docker compose up -d`)
- Eureka Server corriendo en `http://localhost:8761`
- MySQL corriendo en `localhost:3306` — base de datos: `usuariosdb`

## Ejecutar
```bash
cd businessdomain/msvc-usuarios
./mvnw spring-boot:run
```

## Endpoints
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/usuarios | Listar todos |
| GET | /api/usuarios/{id} | Detalle |
| POST | /api/usuarios | Registrar |
| PUT | /api/usuarios/{id} | Actualizar |
| GET | /api/usuarios/{id}/mascotas | Mascotas del usuario (Adapter) |

## Tests
```bash
./mvnw test
```
