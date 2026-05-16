# api-gateway

Punto de entrada único al sistema. Puerto: **8090**

## Rutas configuradas
| Prefijo | Destino |
|---------|---------|
| /bff/** | bff (8080) |
| /api/mascotas/** | msvc-mascotas (8081) |
| /api/usuarios/** | msvc-usuarios (8082) |
| /api/reportes/** | msvc-reportes (8083) |

CORS habilitado para `http://localhost:5173` (frontend).

## Ejecutar
```bash
cd infrastructure/api-gateway
./mvnw spring-boot:run
```
