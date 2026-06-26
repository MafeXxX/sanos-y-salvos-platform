# msvc-reportes

Microservicio de gestión de reportes de mascotas perdidas/halladas. Puerto: **8082**

## Patrón implementado
**Facade** — `MascotaServiceFacade` encapsula la comunicación HTTP con msvc-mascotas.

## Requisitos
- Java 17, Maven 3.8+
- Docker Desktop (MySQL y Keycloak via `docker compose up -d`)
- Eureka Server en `http://localhost:8761`
- msvc-mascotas corriendo en `http://localhost:8081`
- MySQL corriendo en `localhost:3306` — base de datos: `reportesdb`

## Ejecutar
```bash
cd businessdomain/msvc-reportes
./mvnw spring-boot:run
```

## Endpoints
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/reportes | Listar reportes activos |
| GET | /api/reportes/{id} | Detalle |
| POST | /api/reportes | Crear reporte (valida mascota via Facade) |
| PUT | /api/reportes/{id}/estado?estado=FOUND | Cambiar estado |
| DELETE | /api/reportes/{id} | Eliminar |
| GET | /api/reportes/mascota/{id} | Reportes por mascota |

## Estados
`ACTIVE` → `FOUND` → `CLOSED`

## Tests
```bash
./mvnw test
```
