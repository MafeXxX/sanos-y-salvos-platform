Tenemos un proyecto Spring Boot de microservicios llamado Sanos y Salvos.
Es una plataforma veterinaria con esta arquitectura:
- msvc-mascotas (puerto 8081) - patrón Builder
- msvc-reportes (puerto 8082) - patrón Facade
- msvc-usuarios (puerto 8083) - patrón Adapter
- BFF (puerto 8090) - patrón Facade
- API Gateway (puerto 8080)
- Eureka Server (puerto 8761)
- Config Server (puerto 8888)
- Admin Server (puerto 8085)
- Frontend React + Vite (puerto 5173)
- Keycloak via Docker (puerto 9090) - realm: sanosysalvos, client: sanos-y-salvos-client
- MySQL via Docker (puerto 3306) - bases: mascotasdb, reportesdb, usuariosdb

Necesito que revises los READMEs del proyecto y los actualices para que reflejen
la configuración real incluyendo MySQL, Keycloak con Docker, y los puertos correctos.