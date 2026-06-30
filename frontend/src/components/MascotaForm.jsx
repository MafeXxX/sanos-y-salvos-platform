import { useState } from 'react'
import { mascotasApi } from '../services/api'

function MascotaForm({ onGuardado }) {
  const [form, setForm] = useState({ nombre: '', especie: '', raza: '', color: '', edad: '', usuarioId: '' })
  const [error, setError] = useState(null)

  const guardar = (e) => {
    e.preventDefault()
    mascotasApi.crear({ ...form, usuarioId: Number(form.usuarioId), edad: Number(form.edad) })
      .then(onGuardado)
      .catch(e => setError(e.response?.data?.error || e.response?.data || e.message))
  }

  return (
    <form className="form-card" onSubmit={guardar}>
      {error && <div className="alert-error">{error}</div>}
      <div className="form-row">
        <input placeholder="Nombre*" value={form.nombre} onChange={e => setForm({ ...form, nombre: e.target.value })} required />
        <input placeholder="Especie* (Perro, Gato, etc.)" value={form.especie} onChange={e => setForm({ ...form, especie: e.target.value })} required />
        <input placeholder="Raza" value={form.raza} onChange={e => setForm({ ...form, raza: e.target.value })} />
        <input placeholder="Color" value={form.color} onChange={e => setForm({ ...form, color: e.target.value })} />
        <input placeholder="Edad de la mascota" type="number" value={form.edad} onChange={e => setForm({ ...form, edad: e.target.value })} />
        <input placeholder="ID del dueño*" type="number" value={form.usuarioId} onChange={e => setForm({ ...form, usuarioId: e.target.value })} required />
        <button className="btn btn-primary" type="submit">Guardar mascota</button>
      </div>
    </form>
  )
}

export default MascotaForm
