# bff (Backend for Frontend)

Orquesta los tres microservicios y expone datos compuestos al frontend. Puerto: **8090**

## Patrón implementado
**Facade** — `SanosYSalvosFacade` consolida llamadas a mascotas, usuarios y reportes en una sola respuesta.

## Requisitos
- Eureka Server en `http://localhost:8761`
- msvc-mascotas (8081), msvc-usuarios (8083), msvc-reportes (8082) corriendo
- Keycloak en `http://localhost:9090` — realm: `sanosysalvos`, client: `sanos-y-salvos-client`

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
