import axios from 'axios'

const BASE = '/api'
const BFF = '/bff'

export const mascotasApi = {
  listar: () => axios.get(`${BASE}/mascotas`),
  buscar: (id) => axios.get(`${BASE}/mascotas/${id}`),
  crear: (data) => axios.post(`${BASE}/mascotas`, data),
  actualizar: (id, data) => axios.put(`${BASE}/mascotas/${id}`, data),
  porUsuario: (usuarioId) => axios.get(`${BASE}/mascotas/usuario/${usuarioId}`)
}

export const usuariosApi = {
  listar: () => axios.get(`${BASE}/usuarios`),
  buscar: (id) => axios.get(`${BASE}/usuarios/${id}`),
  crear: (data) => axios.post(`${BASE}/usuarios`, data),
  actualizar: (id, data) => axios.put(`${BASE}/usuarios/${id}`, data),
  mascotas: (id) => axios.get(`${BASE}/usuarios/${id}/mascotas`)
}

export const reportesApi = {
  listar: () => axios.get(`${BASE}/reportes`),
  buscar: (id) => axios.get(`${BASE}/reportes/${id}`),
  crear: (data) => axios.post(`${BASE}/reportes`, data),
  cambiarEstado: (id, estado) => axios.put(`${BASE}/reportes/${id}/estado?estado=${estado}`),
  porMascota: (mascotaId) => axios.get(`${BASE}/reportes/mascota/${mascotaId}`)
}

export const bffApi = {
  mascotasConReportes: () => axios.get(`${BFF}/mascotas-con-reportes`),
  reportesActivos: () => axios.get(`${BFF}/reportes-activos`),
  mascotaConPropietario: (id) => axios.get(`${BFF}/mascota/${id}/propietario`)
}
