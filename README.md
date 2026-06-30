# Sanos y Salvos Platform

Plataforma de gestión veterinaria desarrollada con arquitectura de microservicios.  
**DSY1106 Desarrollo Fullstack III — Evaluación Parcial N°3**

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
                        │ + Circuit Br.│
                        └──────┬───────┘
                               │
                        ┌──────▼───────┐
                        │     BFF      │  :8090
                        │ + Circuit Br.│
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
               │
        ┌──────▼──────┐   ┌──────────────┐
        │ Config Srv  │   │ Admin Server │
        │    :8888    │   │    :8085     │
        └─────────────┘   └──────────────┘
```

---

## Componentes

| Componente | Puerto | Qué hace | Patrón |
|---|---|---|---|
| **msvc-mascotas** | 8081 | CRUD completo de mascotas; validación @NotBlank + @NotNull + Feign a usuarios | Builder |
| **msvc-reportes** | 8082 | CRUD de reportes de mascotas perdidas/halladas; consulta msvc-mascotas via Feign | Facade |
| **msvc-usuarios** | 8083 | CRUD completo de usuarios; expone las mascotas asociadas a cada uno | Adapter |
| **BFF** | 8090 | Orquesta los tres microservicios con Circuit Breaker + Retry (Resilience4j) | Facade |
| **API Gateway** | 8080 | Punto de entrada único; enruta con Circuit Breaker, aplica seguridad OAuth2 | — |
| **Eureka Server** | 8761 | Registro y descubrimiento de servicios | — |
| **Config Server** | 8888 | Configuración centralizada para BFF y microservicios | — |
| **Admin Server** | 8085 | Monitoreo y métricas (Spring Boot Admin) — recibe clientes de todos los servicios | — |
| **Keycloak** | 9090 | Servidor de autenticación OAuth2/OIDC (Docker) | — |
| **Keycloak Adapter** | — | Librería compartida de seguridad JWT usada por el API Gateway | — |
| **MySQL** | 3306 | Base de datos relacional — 3 esquemas separados (Docker) | — |
| **Frontend** | 5173 | Interfaz React + Vite con autenticación via Keycloak JS | — |

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

### 4. Admin Server

```bash
cd infrastructure/admin-server
mvn spring-boot:run
```

Dashboard: http://localhost:8085

---

### 5. Microservicios (cada uno en su propia terminal)

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

### 6. BFF

```bash
cd infrastructure/bff
mvn spring-boot:run
```

Disponible en: http://localhost:8090

---

### 7. API Gateway

```bash
cd infrastructure/api-gateway
mvn spring-boot:run
```

Punto de entrada único: http://localhost:8080

---

### 8. Frontend

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

## Documentación de API (Swagger)

Cada servicio expone Swagger UI con todos los endpoints, parámetros y ejemplos:

| Servicio | Swagger UI |
|---|---|
| msvc-mascotas | http://localhost:8081/swagger-ui.html |
| msvc-reportes | http://localhost:8082/swagger-ui.html |
| msvc-usuarios | http://localhost:8083/swagger-ui.html |
| BFF | http://localhost:8090/swagger-ui.html |

---

## Pruebas

```bash
# Ejecutar todos los tests con reporte de cobertura
mvn verify
```

### Cobertura con JaCoCo

El proyecto usa **JaCoCo 0.8.12** para medir cobertura de código. Los reportes HTML se generan en `target/site/jacoco/` de cada módulo al ejecutar `mvn verify`.

| Microservicio | Tests | Cobertura |
|---|---|---|
| msvc-mascotas | 24 | >88% |
| msvc-reportes | 16 | >70% |
| msvc-usuarios | 16 | >70% |

**Tipos de pruebas implementadas:**

| Tipo | Cantidad | Descripción |
|---|---|---|
| Unitarias (Mockito) | 30+ | Servicios, builders, excepciones |
| Controller (@WebMvcTest) | 19 | Endpoints REST con MockMvc |
| Integración (@SpringBootTest + H2) | 3 | Flujo controller → service → repository |
| End-to-End (TestRestTemplate) | 3 | Flujo HTTP completo con puerto aleatorio |

### Datos de demostración

Ver `docs/datos-demostracion.md` para el paso a paso del Video de Uso.

---

## Patrones de diseño

| Patrón | Componente | Descripción |
|---|---|---|
| **Builder** | msvc-mascotas | Construcción y validación fluida del objeto Mascota |
| **Facade** | msvc-reportes, bff | Encapsula la comunicación entre servicios en una interfaz simple |
| **Adapter** | msvc-usuarios | Adapta las respuestas de msvc-mascotas al formato esperado |
| **Circuit Breaker** | bff, api-gateway | Resilience4j — tolerancia a fallos y degradación controlada |
| **Retry** | bff | Reintentos automáticos ante fallos transitorios |

Ver análisis completo en `docs/analisis-patrones.pdf`.

---

## Tolerancia a fallos

El BFF y el API Gateway implementan **Circuit Breaker** con Resilience4j:

| Componente | Mecanismo |
|---|---|
| **BFF** | `@CircuitBreaker` + `@Retry` en los 3 métodos del Facade, con fallbacks que devuelven listas vacías |
| **API Gateway** | Filtro `CircuitBreaker` en cada ruta, con `FallbackController` que responde HTTP 503 |

---

## Configuración centralizada

El **Config Server** (:8888) sirve configuración compartida a BFF y microservicios:

```
infrastructure/config-server/src/main/resources/config/
  bff.properties
  msvc-mascotas.properties
  msvc-reportes.properties
  msvc-usuarios.properties
```

Cada servicio tiene su propio `bootstrap.yml` que apunta a `http://localhost:8888`.

---

## Manejo de excepciones

Cada microservicio utiliza un `@ControllerAdvice` global (`GlobalExceptionHandler`) que centraliza las respuestas de error en formato JSON:

- `RuntimeException` → `404 Not Found` → `{"error": "mensaje"}`
- `IllegalArgumentException` → `400 Bad Request` → `{"error": "mensaje"}`

## Logging

Cada microservicio y el BFF tienen configuración **logback-spring.xml** con:

- Salida a **consola con colores** (nivel y logger resaltados)
- Archivo **rotativo** en `logs/` (10 MB por archivo, máximo 7 días, 100 MB total)
- `DEBUG` para el código del proyecto, `INFO` para Spring, `WARN` para Hibernate y Netflix

## Validaciones

- **Mascota**: `@NotBlank` en nombre y especie, `@NotNull` en usuarioId, más validación cruzada via Feign (UsuarioServiceFacade)
- **Usuario**: `@NotBlank` en nombre y email, `@Email` en email, unicidad validada en el servicio
- **Reporte**: ubicación obligatoria validada en el servicio, `@Valid` en el controller

---

## Repositorios

Ver `repositorios.txt` para los enlaces a los repositorios individuales de cada componente.
