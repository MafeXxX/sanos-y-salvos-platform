# msvc-mascotas

Microservicio de gestión de mascotas. Puerto: **8081**

## Patrón implementado
**Builder** — `MascotaBuilder` valida nombre, especie y usuarioId antes de persistir.

## Requisitos
- Java 17, Maven 3.8+
- Docker Desktop (MySQL y Keycloak via `docker compose up -d`)
- Eureka Server corriendo en `http://localhost:8761`
- MySQL corriendo en `localhost:3306` — base de datos: `mascotasdb`

## Ejecutar
```bash
cd businessdomain/msvc-mascotas
./mvnw spring-boot:run
```

## Endpoints
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/mascotas | Listar todas |
| GET | /api/mascotas/{id} | Detalle |
| POST | /api/mascotas | Registrar (usa Builder) |
| PUT | /api/mascotas/{id} | Actualizar |
| GET | /api/mascotas/usuario/{id} | Mascotas por usuario |

## Tests
```bash
./mvnw test
```
