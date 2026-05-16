import { useEffect, useState } from 'react'
import { usuariosApi } from '../services/api'

function UsuariosPage() {
  const [usuarios, setUsuarios] = useState([])
  const [form, setForm] = useState({ nombre: '', email: '', telefono: '', direccion: '' })
  const [error, setError] = useState(null)

  const cargar = () => {
    usuariosApi.listar()
      .then(r => setUsuarios(r.data))
      .catch(e => setError(e.message))
  }

  useEffect(() => { cargar() }, [])

  const guardar = (e) => {
    e.preventDefault()
    usuariosApi.crear(form)
      .then(() => { cargar(); setForm({ nombre: '', email: '', telefono: '', direccion: '' }) })
      .catch(e => setError(e.response?.data || e.message))
  }

  return (
    <div>
      <h2>Usuarios</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={guardar} style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap', marginBottom: '1rem' }}>
        <input placeholder="Nombre*" value={form.nombre} onChange={e => setForm({ ...form, nombre: e.target.value })} required />
        <input placeholder="Email*" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} required />
        <input placeholder="Teléfono" value={form.telefono} onChange={e => setForm({ ...form, telefono: e.target.value })} />
        <input placeholder="Dirección" value={form.direccion} onChange={e => setForm({ ...form, direccion: e.target.value })} />
        <button type="submit">Registrar</button>
      </form>
      <table border="1" cellPadding="8" style={{ borderCollapse: 'collapse' }}>
        <thead>
          <tr><th>ID</th><th>Nombre</th><th>Email</th><th>Teléfono</th></tr>
        </thead>
        <tbody>
          {usuarios.map(u => (
            <tr key={u.id}>
              <td>{u.id}</td><td>{u.nombre}</td><td>{u.email}</td><td>{u.telefono || '-'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default UsuariosPage
