import { useEffect, useState } from 'react'
import { usuariosApi } from '../services/api'

function UsuariosPage() {
  const [usuarios, setUsuarios] = useState([])
  const [form, setForm] = useState({ nombre: '', email: '', telefono: '', direccion: '' })
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)

  const cargar = () => {
    setLoading(true)
    usuariosApi.listar()
      .then(r => setUsuarios(r.data))
      .catch(e => setError(e.message))
      .finally(() => setLoading(false))
  }

  useEffect(() => { cargar() }, [])

  const guardar = (e) => {
    e.preventDefault()
    usuariosApi.crear(form)
      .then(() => { cargar(); setForm({ nombre: '', email: '', telefono: '', direccion: '' }) })
      .catch(e => setError(e.response?.data || e.message))
  }

  const eliminar = (id) => {
    if (!window.confirm('¿Eliminar este usuario?')) return
    usuariosApi.eliminar(id).then(cargar).catch(e => setError(e.message))
  }

  return (
    <div>
      <h2 className="page-title">👤 Usuarios</h2>
      {error && <div className="alert-error">{error}</div>}

      <form className="form-card" onSubmit={guardar}>
        <div className="form-row">
          <input placeholder="Nombre completo*" value={form.nombre} onChange={e => setForm({ ...form, nombre: e.target.value })} required />
          <input placeholder="Correo electrónico*" type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} required />
          <input placeholder="Teléfono" value={form.telefono} onChange={e => setForm({ ...form, telefono: e.target.value })} />
          <input placeholder="Dirección" value={form.direccion} onChange={e => setForm({ ...form, direccion: e.target.value })} />
          <button className="btn btn-primary" type="submit">Registrar usuario</button>
        </div>
      </form>

      {loading ? (
        <div className="loading">Cargando usuarios...</div>
      ) : usuarios.length === 0 ? (
        <div className="empty-state">
          <p>👥 No hay usuarios registrados. ¡Registrá el primero!</p>
        </div>
      ) : (
        <div className="table-card">
          <table>
            <thead>
              <tr><th>ID</th><th>Nombre</th><th>Email</th><th>Teléfono</th><th>Dirección</th><th>Acciones</th></tr>
            </thead>
            <tbody>
              {usuarios.map(u => (
                <tr key={u.id}>
                  <td>{u.id}</td>
                  <td>{u.nombre}</td>
                  <td>{u.email}</td>
                  <td>{u.telefono || '—'}</td>
                  <td>{u.direccion || '—'}</td>
                  <td className="actions">
                    <button className="btn btn-danger btn-sm" onClick={() => eliminar(u.id)}>Eliminar</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}

export default UsuariosPage
