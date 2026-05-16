# Sanos y Salvos Platform

Plataforma de gestión veterinaria desarrollada con arquitectura de microservicios.  
**DSY1106 Desarrollo Fullstack III — Evaluación Parcial N°2**

## Equipo

| Integrante |
|---|
| Benjamin Arellano Gallardo |
| Benjamin Valdebenito |
| Maximiliano Vera |
| Matias Guzman |

---

## Arquitectura

```
                        ┌──────────────┐
                        │   Frontend   │
                        │ React + Vite │
                        └──────┬───────┘
                               │
                   ┌───────────▼───────────┐
                   │      Keycloak         │  :9090  (Docker)
                   │  Autenticación OAuth2 │
                   └───────────┬───────────┘
                               │
                        ┌──────▼───────┐
                        │ API Gateway  │  :8080
                        └──────┬───────┘
                               │
                        ┌──────▼───────┐
                        │     BFF      │  :8090
                        └──┬───┬───┬───┘
                           │   │   │
               ┌───────────┘   │   └───────────┐
               │               │               │
        ┌──────▼──────┐ ┌──────▼──────┐ ┌──────▼──────┐
        │  Mascotas   │ │  Reportes   │ │  Usuarios   │
        │    :8081    │ │    :8082    │ │    :8083    │
        └──────┬──────┘ └──────┬──────┘ └──────┬──────┘
               │               │               │
        ┌──────▼───────────────▼───────────────▼──────┐
        │                  MySQL :3306                 │
        │   mascotasdb      reportesdb    usuariosdb   │
        └──────────────────────────────────────────────┘
               │               │               │
        ┌──────▼───────────────▼───────────────▼──────┐
        │               Eureka Server :8761            │
        └─────────────────────────────────────────────┘
```

---

## Componentes

| Componente | Puerto | Qué hace | Patrón |
|---|---|---|---|
| **msvc-mascotas** | 8081 | CRUD de mascotas; registra nombre, especie y dueño | Builder |
| **msvc-reportes** | 8082 | Gestiona reportes de mascotas perdidas/halladas; consulta msvc-mascotas | Facade |
| **msvc-usuarios** | 8083 | CRUD de usuarios; expone las mascotas asociadas a cada uno | Adapter |
| **BFF** | 8090 | Orquesta los tres microservicios en respuestas compuestas para el frontend | Facade |
| **API Gateway** | 8080 | Punto de entrada único; enruta y aplica seguridad OAuth2 | — |
| **Eureka Server** | 8761 | Registro y descubrimiento de servicios | — |
| **Config Server** | 8888 | Configuración centralizada para todos los servicios | — |
| **Admin Server** | 8085 | Monitoreo y métricas (Spring Boot Admin) | — |
| **Keycloak** | 9090 | Servidor de autenticación OAuth2/OIDC (Docker) | — |
| **MySQL** | 3306 | Base de datos relacional — 3 esquemas separados (Docker) | — |
| **Frontend** | 5173 | Interfaz React + Vite con autenticación via Keycloak | — |

---

## Prerrequisitos

- **Java 17+** y **Maven 3.8+**
- **Node.js 18+** y **npm**
- **Docker Desktop**

---

## Inicio del proyecto

El orden de arranque es importante.

### 1. Docker (Keycloak + MySQL)

```bash
docker compose up -d
```

Levanta Keycloak en http://localhost:9090 y MySQL en el puerto 3306.  
El realm `sanosysalvos` se importa automáticamente desde `keycloak/realm-export.json`.

Para detener:
```bash
docker compose down
```

---

### 2. Eureka Server

```bash
cd infrastructure/eureka-server
mvn spring-boot:run
```

Dashboard: http://localhost:8761

---

### 3. Config Server

```bash
cd infrastructure/config-server
mvn spring-boot:run
```

---

### 4. Microservicios (cada uno en su propia terminal)

```bash
# Terminal 1 — mascotasdb
cd businessdomain/msvc-mascotas
mvn spring-boot:run

# Terminal 2 — reportesdb
cd businessdomain/msvc-reportes
mvn spring-boot:run

# Terminal 3 — usuariosdb
cd businessdomain/msvc-usuarios
mvn spring-boot:run
```

---

### 5. BFF

```bash
cd infrastructure/bff
mvn spring-boot:run
```

Disponible en: http://localhost:8090

---

### 6. API Gateway

```bash
cd infrastructure/api-gateway
mvn spring-boot:run
```

Punto de entrada único: http://localhost:8080

---

### 7. Frontend

```bash
cd frontend
npm install
npm run dev
```

Aplicación en: http://localhost:5173

---

## Bases de datos (MySQL)

| Base de datos | Microservicio |
|---|---|
| `mascotasdb` | msvc-mascotas |
| `reportesdb` | msvc-reportes |
| `usuariosdb` | msvc-usuarios |

Todas en `localhost:3306`. Las tablas se crean automáticamente con `mysql/init.sql` al levantar Docker.

---

## Autenticación con Keycloak

| Propiedad | Valor |
|---|---|
| Realm | `sanosysalvos` |
| Client ID | `sanos-y-salvos-client` |
| URL Keycloak | http://localhost:9090 |
| Redirect URI | `http://localhost:5173/*` |

Panel de administración: http://localhost:9090/admin — `admin` / `admin`

Usuario de prueba: `usuario-test` / `admin123`

---

## Pruebas

```bash
# Desde la raíz de cualquier microservicio
mvn test
```

---

## Patrones de diseño

| Patrón | Componente | Descripción |
|---|---|---|
| **Builder** | msvc-mascotas | Construcción y validación fluida del objeto Mascota |
| **Facade** | msvc-reportes, bff | Encapsula la comunicación entre servicios en una interfaz simple |
| **Adapter** | msvc-usuarios | Adapta las respuestas de msvc-mascotas al formato esperado |

Ver análisis completo en `docs/analisis-patrones.pdf`.

---

## Repositorios

Ver `repositorios.txt` para los enlaces a los repositorios individuales de cada componente.
