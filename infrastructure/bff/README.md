# bff (Backend for Frontend)

Orquesta los tres microservicios y expone datos compuestos al frontend. Puerto: **8090**

## Patrones implementados
- **Facade** — `SanosYSalvosFacade` consolida llamadas a mascotas, usuarios y reportes en una sola respuesta.
- **Circuit Breaker** — `@CircuitBreaker` + `@Retry` (Resilience4j) en los 3 métodos del facade, con fallbacks que degradan a listas vacías.

## Requisitos
- Eureka Server en `http://localhost:8761`
- Config Server en `http://localhost:8888`
- msvc-mascotas (8081), msvc-usuarios (8083), msvc-reportes (8082) corriendo
- Keycloak en `http://localhost:9090` — realm: `sanosysalvos`, client: `sanos-y-salvos-client`
- Admin Server en `http://localhost:8085`

## Ejecutar
```bash
cd infrastructure/bff
./mvnw spring-boot:run
```

## Endpoints
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /bff/mascotas-con-reportes | Mascotas con sus reportes activos |
| GET | /bff/reportes-activos | Reportes con datos de mascota y dueño |
| GET | /bff/mascota/{id}/propietario | Mascota con datos del propietario |
