import { useEffect, useState } from 'react'
import { mascotasApi } from '../services/api'
import MascotaForm from '../components/MascotaForm'

function MascotasPage() {
  const [mascotas, setMascotas] = useState([])
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)
  const [mostrarForm, setMostrarForm] = useState(false)

  const cargar = () => {
    setLoading(true)
    mascotasApi.listar()
      .then(r => setMascotas(r.data))
      .catch(e => setError(e.response?.data?.error || e.message))
      .finally(() => setLoading(false))
  }

  useEffect(() => { cargar() }, [])

  const eliminar = (id) => {
    if (!window.confirm('¿Eliminar esta mascota?')) return
    mascotasApi.eliminar(id).then(cargar).catch(e => setError(e.response?.data?.error || e.message))
  }

  return (
    <div>
      <h2 className="page-title">🐶 Mascotas registradas</h2>
      {error && <div className="alert-error">{error}</div>}

      <button className="btn btn-primary" onClick={() => setMostrarForm(!mostrarForm)} style={{ marginBottom: '1rem' }}>
        {mostrarForm ? '✕ Cancelar' : '+ Nueva mascota'}
      </button>
      {mostrarForm && <MascotaForm onGuardado={() => { setMostrarForm(false); cargar() }} />}

      {loading ? (
        <div className="loading">Cargando mascotas...</div>
      ) : mascotas.length === 0 ? (
        <div className="empty-state">
          <p>🐾 No hay mascotas registradas. ¡Agregá la primera!</p>
        </div>
      ) : (
        <div className="table-card">
          <table>
            <thead>
              <tr><th>ID</th><th>Nombre</th><th>Especie</th><th>Raza</th><th>Color</th><th>Edad</th><th>Reporte</th><th>Acciones</th></tr>
            </thead>
            <tbody>
              {mascotas.map(m => (
                <tr key={m.id}>
                  <td>{m.id}</td>
                  <td>{m.nombre}</td>
                  <td>{m.especie}</td>
                  <td>{m.raza || '—'}</td>
                  <td>{m.color || '—'}</td>
                  <td>{m.edad}</td>
                  <td><span className={`badge ${m.tieneReporteActivo ? 'badge-yes' : 'badge-no'}`}>{m.tieneReporteActivo ? 'Sí' : 'No'}</span></td>
                  <td className="actions">
                    <button className="btn btn-danger btn-sm" onClick={() => eliminar(m.id)}>Eliminar</button>
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

export default MascotasPage
