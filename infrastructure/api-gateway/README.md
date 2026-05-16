# api-gateway

Punto de entrada único al sistema. Puerto: **8080**

## Rutas configuradas
| Prefijo | Destino |
|---------|---------|
| /bff/** | bff (8090) |
| /api/mascotas/** | msvc-mascotas (8081) |
| /api/usuarios/** | msvc-usuarios (8083) |
| /api/reportes/** | msvc-reportes (8082) |

CORS habilitado para `http://localhost:5173` (frontend).

## Requisitos
- Eureka Server en `http://localhost:8761`
- Keycloak en `http://localhost:9090` — realm: `sanosysalvos`, client: `sanos-y-salvos-client`

## Ejecutar
```bash
cd infrastructure/api-gateway
./mvnw spring-boot:run
```
