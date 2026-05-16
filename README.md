# 🐾 Sanos y Salvos Platform

Plataforma de gestión veterinaria desarrollada con arquitectura de microservicios.  
**DSY1106 Desarrollo Fullstack III — Evaluación Parcial N°2**

## 👥 Equipo

| Integrante |
|---|
| Benjamin Arellano Gallardo |
| Benjamin Valdebenito |
| Maximiliano Vera |
| Matias Guzman |

---

## 📐 Arquitectura

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
        └─────────────┘ └─────────────┘ └─────────────┘
               │               │               │
        ┌──────▼───────────────▼───────────────▼──────┐
        │               Eureka Server :8761            │
        └─────────────────────────────────────────────┘
```

---

## 🗂️ Estructura del Proyecto

```
sanos-y-salvos/
├── docker-compose.yml           # Configuración Docker (Keycloak)
├── keycloak/
│   └── realm-export.json        # Realm preconfigurado (importación automática)
├── frontend/                    # Aplicación React + Vite
│   └── src/
│       ├── components/          # Componentes reutilizables
│       ├── pages/               # Páginas (Mascotas, Reportes, Usuarios)
│       └── services/            # Axios + Keycloak
├── businessdomain/
│   ├── msvc-mascotas/           # Microservicio mascotas — patrón Builder
│   ├── msvc-reportes/           # Microservicio reportes — patrón Facade
│   └── msvc-usuarios/           # Microservicio usuarios — patrón Adapter
├── infrastructure/
│   ├── bff/                     # Backend For Frontend — patrón Facade
│   ├── api-gateway/             # Spring Cloud Gateway
│   ├── eureka-server/           # Service Discovery
│   ├── config-server/           # Configuración centralizada
│   ├── admin-server/            # Spring Boot Admin
│   └── keycloak-adapter/        # Adaptador de seguridad OAuth2
├── arquetipos/
│   ├── arquetipo-microservicio/ # Arquetipo Maven base para microservicios
│   └── arquetipo-bff/           # Arquetipo Maven base para BFF
└── docs/
    ├── analisis-patrones.pdf    # Análisis de patrones de diseño
    └── plan-branching.pdf       # Estrategia de branching
```

---

## ⚙️ Prerrequisitos

- **Java 17** o superior
- **Maven 3.8+**
- **Node.js 18+** y **npm**
- **Docker Desktop** (para Keycloak)
- **Git**

---

## 🚀 Cómo iniciar el proyecto

El orden de arranque es importante. Keycloak debe iniciarse primero.

### 1. Keycloak (Docker)

```bash
docker compose up -d
```

Keycloak queda disponible en: http://localhost:9090  
El realm `sanosysalvos` se importa automáticamente con todo preconfigurado.

> **Usuario de prueba:**  
> Usuario: `usuario-test` | Contraseña: `admin123`

Para detenerlo:
```bash
docker compose down
```

---

### 2. Eureka Server

```bash
cd infrastructure/eureka-server
mvn spring-boot:run
```

Dashboard en: http://localhost:8761

---

### 3. Config Server

```bash
cd infrastructure/config-server
mvn spring-boot:run
```

---

### 4. Microservicios

```bash
# Terminal 1
cd businessdomain/msvc-mascotas
mvn spring-boot:run

# Terminal 2
cd businessdomain/msvc-reportes
mvn spring-boot:run

# Terminal 3
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

## 🔌 Puertos

| Servicio | Puerto |
|---|---|
| Frontend | 5173 |
| Keycloak | 9090 |
| API Gateway | 8080 |
| BFF | 8090 |
| msvc-mascotas | 8081 |
| msvc-reportes | 8082 |
| msvc-usuarios | 8083 |
| Eureka Server | 8761 |
| Admin Server | 8085 |
| Config Server | 8888 |

---

## 🔐 Autenticación con Keycloak

La plataforma usa Keycloak como servidor de autenticación OAuth2. La configuración del realm se importa automáticamente desde `keycloak/realm-export.json` al levantar Docker.

**Configuración del client:**

| Propiedad | Valor |
|---|---|
| Realm | `sanosysalvos` |
| Client ID | `sanos-y-salvos-client` |
| URL Keycloak | `http://localhost:9090` |
| Redirect URI | `http://localhost:5173/*` |

Para acceder al panel de administración de Keycloak:  
http://localhost:9090/admin — usuario: `admin` / contraseña: `admin`

---

## 🧪 Pruebas unitarias

```bash
# Desde la raíz de cualquier microservicio
mvn test
```

---

## 🎨 Patrones de Diseño Implementados

| Patrón | Componente | Descripción |
|---|---|---|
| **Builder** | msvc-mascotas | Construcción fluida de objetos Mascota |
| **Facade** | msvc-reportes, bff | Simplificación de subsistemas complejos |
| **Adapter** | msvc-usuarios | Adaptación entre modelos de microservicios |

Ver análisis completo en `docs/analisis-patrones.pdf`.

---

## 🌿 Estrategia de Branching

```
main          ←── develop (merge final)
develop       ←── feature/* (merge por componente)
feature/msvc-mascotas
feature/msvc-reportes
feature/msvc-usuarios
feature/bff
feature/infrastructure
feature/frontend
feature/arquetipos
feature/docs
feature/docker-keycloak
```

Ver detalles en `docs/plan-branching.pdf`.

---

## 📁 Repositorios

Ver `repositorios.txt` para los enlaces a los repositorios individuales de cada componente.