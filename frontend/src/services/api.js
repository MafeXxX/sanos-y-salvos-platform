import axios from 'axios'

const BASE = '/api'
const BFF = '/bff'

// Retry automático cuando el Circuit Breaker devuelve 503 (servicios iniciando)
axios.interceptors.response.use(
  response => response,
  async error => {
    const config = error.config
    if (error.response?.status === 503 && (!config._retryCount || config._retryCount < 5)) {
      config._retryCount = (config._retryCount || 0) + 1
      await new Promise(r => setTimeout(r, 2000))
      return axios(config)
    }
    return Promise.reject(error)
  }
)

export const mascotasApi = {
  listar: () => axios.get(`${BASE}/mascotas`),
  buscar: (id) => axios.get(`${BASE}/mascotas/${id}`),
  crear: (data) => axios.post(`${BASE}/mascotas`, data),
  actualizar: (id, data) => axios.put(`${BASE}/mascotas/${id}`, data),
  eliminar: (id) => axios.delete(`${BASE}/mascotas/${id}`),
  porUsuario: (usuarioId) => axios.get(`${BASE}/mascotas/usuario/${usuarioId}`)
}

export const usuariosApi = {
  listar: () => axios.get(`${BASE}/usuarios`),
  buscar: (id) => axios.get(`${BASE}/usuarios/${id}`),
  crear: (data) => axios.post(`${BASE}/usuarios`, data),
  actualizar: (id, data) => axios.put(`${BASE}/usuarios/${id}`, data),
  eliminar: (id) => axios.delete(`${BASE}/usuarios/${id}`),
  mascotas: (id) => axios.get(`${BASE}/usuarios/${id}/mascotas`)
}

export const reportesApi = {
  listar: () => axios.get(`${BASE}/reportes`),
  buscar: (id) => axios.get(`${BASE}/reportes/${id}`),
  crear: (data) => axios.post(`${BASE}/reportes`, data),
  cambiarEstado: (id, estado) => axios.put(`${BASE}/reportes/${id}/estado?estado=${estado}`),
  eliminar: (id) => axios.delete(`${BASE}/reportes/${id}`),
  porMascota: (mascotaId) => axios.get(`${BASE}/reportes/mascota/${mascotaId}`)
}

export const bffApi = {
  mascotasConReportes: () => axios.get(`${BFF}/mascotas-con-reportes`),
  reportesActivos: () => axios.get(`${BFF}/reportes-activos`),
  mascotaConPropietario: (id) => axios.get(`${BFF}/mascota/${id}/propietario`)
}
