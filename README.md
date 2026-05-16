# Sanos y Salvos Platform

Proyecto fullstack — Evaluación Parcial N°2, Desarrollo Fullstack III (DSY1106).

## Estructura

```
sanos-y-salvos/
├── frontend/               # React + Vite (NPM)
├── businessdomain/
│   ├── msvc-mascotas/      # Microservicio mascotas (Builder)
│   ├── msvc-reportes/      # Microservicio reportes (Facade)
│   └── msvc-usuarios/      # Microservicio usuarios (Adapter)
├── infrastructure/
│   ├── bff/                # Backend For Frontend (Facade)
│   ├── api-gateway/        # Spring Cloud Gateway
│   ├── eureka-server/      # Service Discovery
│   ├── config-server/      # Configuración centralizada
│   ├── admin-server/       # Spring Boot Admin
│   └── keycloak-adapter/   # Autenticación
├── arquetipos/             # Arquetipos Maven
└── docs/                   # Documentación
```

## Branching
- `main` → producción estable
- `develop` → integración
- `feature/<nombre>` → desarrollo por componente
- `hotfix/<nombre>` → correcciones urgentes
