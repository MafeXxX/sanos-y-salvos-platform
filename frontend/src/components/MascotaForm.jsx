import { useState } from 'react'
import { mascotasApi } from '../services/api'

function MascotaForm({ onGuardado }) {
  const [form, setForm] = useState({ nombre: '', especie: '', raza: '', color: '', edad: 0, usuarioId: '' })
  const [error, setError] = useState(null)

  const guardar = (e) => {
    e.preventDefault()
    mascotasApi.crear({ ...form, usuarioId: Number(form.usuarioId), edad: Number(form.edad) })
      .then(onGuardado)
      .catch(e => setError(e.response?.data || e.message))
  }

  return (
    <form onSubmit={guardar} style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap', margin: '1rem 0', padding: '1rem', border: '1px solid #ccc' }}>
      {error && <p style={{ color: 'red', width: '100%' }}>{error}</p>}
      <input placeholder="Nombre*" value={form.nombre} onChange={e => setForm({ ...form, nombre: e.target.value })} required />
      <input placeholder="Especie*" value={form.especie} onChange={e => setForm({ ...form, especie: e.target.value })} required />
      <input placeholder="Raza" value={form.raza} onChange={e => setForm({ ...form, raza: e.target.value })} />
      <input placeholder="Color" value={form.color} onChange={e => setForm({ ...form, color: e.target.value })} />
      <input placeholder="Edad" type="number" value={form.edad} onChange={e => setForm({ ...form, edad: e.target.value })} />
      <input placeholder="ID Usuario*" type="number" value={form.usuarioId} onChange={e => setForm({ ...form, usuarioId: e.target.value })} required />
      <button type="submit">Guardar</button>
    </form>
  )
}

export default MascotaForm
