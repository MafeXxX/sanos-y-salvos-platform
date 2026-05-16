# frontend — Sanos y Salvos UI

Aplicación React con Vite. Puerto: **5173**

## Requisitos
- Node.js 18+, NPM
- API Gateway corriendo en `http://localhost:8080`
- Keycloak corriendo en `http://localhost:9090` (via `docker compose up -d`)
  - Realm: `sanosysalvos`
  - Client ID: `sanos-y-salvos-client`

## Instalar y ejecutar
```bash
cd frontend
npm install
npm run dev
```

## Páginas
- `/` — Listado y registro de mascotas
- `/usuarios` — Listado y registro de usuarios
- `/reportes` — Reportes activos, crear y cambiar estado
