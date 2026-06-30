import { useEffect, useState } from 'react'
import { reportesApi } from '../services/api'

const ESTADO_LABEL = { ACTIVE: 'Activo', FOUND: 'Encontrado', CLOSED: 'Cerrado' }
const ESTADO_BADGE = { ACTIVE: 'badge-active', FOUND: 'badge-found', CLOSED: 'badge-closed' }

function ReportesPage() {
  const [reportes, setReportes] = useState([])
  const [form, setForm] = useState({ mascotaId: '', ubicacion: '', tipoReporte: 'PERDIDA', descripcion: '' })
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)

  const cargar = () => {
    setLoading(true)
    reportesApi.listar()
      .then(r => setReportes(r.data))
      .catch(e => setError(e.response?.data?.error || e.message))
      .finally(() => setLoading(false))
  }

  useEffect(() => { cargar() }, [])

  const guardar = (e) => {
    e.preventDefault()
    reportesApi.crear({ ...form, mascotaId: Number(form.mascotaId) })
      .then(() => { cargar(); setForm({ mascotaId: '', ubicacion: '', tipoReporte: 'PERDIDA', descripcion: '' }) })
      .catch(e => setError(e.response?.data?.error || e.response?.data || e.message))
  }

  const cambiarEstado = (id, estado) => {
    reportesApi.cambiarEstado(id, estado).then(cargar).catch(e => setError(e.response?.data?.error || e.message))
  }

  const eliminar = (id) => {
    if (!window.confirm('¿Eliminar este reporte?')) return
    reportesApi.eliminar(id).then(cargar).catch(e => setError(e.response?.data?.error || e.message))
  }

  return (
    <div>
      <h2 className="page-title">📋 Reportes</h2>
      {error && <div className="alert-error">{error}</div>}

      <form className="form-card" onSubmit={guardar}>
        <div className="form-row">
          <input placeholder="ID de la mascota*" type="number" value={form.mascotaId} onChange={e => setForm({ ...form, mascotaId: e.target.value })} required />
          <input placeholder="Ubicación*" value={form.ubicacion} onChange={e => setForm({ ...form, ubicacion: e.target.value })} required />
          <select value={form.tipoReporte} onChange={e => setForm({ ...form, tipoReporte: e.target.value })}>
            <option value="PERDIDA">🐾 Pérdida</option>
            <option value="HALLAZGO">📍 Hallazgo</option>
          </select>
          <input placeholder="Descripción" value={form.descripcion} onChange={e => setForm({ ...form, descripcion: e.target.value })} />
          <button className="btn btn-primary" type="submit">Crear reporte</button>
        </div>
      </form>

      {loading ? (
        <div className="loading">Cargando reportes...</div>
      ) : reportes.length === 0 ? (
        <div className="empty-state">
          <p>📭 No hay reportes activos.</p>
        </div>
      ) : (
        <div className="table-card">
          <table>
            <thead>
              <tr><th>ID</th><th>Mascota #</th><th>Ubicación</th><th>Tipo</th><th>Estado</th><th>Fecha</th><th>Acciones</th></tr>
            </thead>
            <tbody>
              {reportes.map(r => (
                <tr key={r.id}>
                  <td>{r.id}</td>
                  <td>{r.mascotaId}</td>
                  <td>{r.ubicacion}</td>
                  <td>{r.tipoReporte === 'PERDIDA' ? '🐾 Pérdida' : '📍 Hallazgo'}</td>
                  <td><span className={`badge ${ESTADO_BADGE[r.estado]}`}>{ESTADO_LABEL[r.estado] || r.estado}</span></td>
                  <td>{r.fechaRegistro}</td>
                  <td className="actions">
                    {r.estado === 'ACTIVE' && (
                      <>
                        <button className="btn btn-primary btn-sm" onClick={() => cambiarEstado(r.id, 'FOUND')}>✓ Encontrada</button>
                        <button className="btn btn-outline btn-sm" onClick={() => cambiarEstado(r.id, 'CLOSED')}>Cerrar</button>
                      </>
                    )}
                    <button className="btn btn-danger btn-sm" onClick={() => eliminar(r.id)}>Eliminar</button>
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

export default ReportesPage
