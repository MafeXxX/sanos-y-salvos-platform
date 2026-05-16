import { useEffect, useState } from 'react'
import { reportesApi } from '../services/api'

function ReportesPage() {
  const [reportes, setReportes] = useState([])
  const [form, setForm] = useState({ mascotaId: '', ubicacion: '', tipoReporte: 'PERDIDA', descripcion: '' })
  const [error, setError] = useState(null)

  const cargar = () => {
    reportesApi.listar()
      .then(r => setReportes(r.data))
      .catch(e => setError(e.message))
  }

  useEffect(() => { cargar() }, [])

  const guardar = (e) => {
    e.preventDefault()
    reportesApi.crear({ ...form, mascotaId: Number(form.mascotaId) })
      .then(() => { cargar(); setForm({ mascotaId: '', ubicacion: '', tipoReporte: 'PERDIDA', descripcion: '' }) })
      .catch(e => setError(e.response?.data || e.message))
  }

  const cambiarEstado = (id, estado) => {
    reportesApi.cambiarEstado(id, estado).then(cargar).catch(e => setError(e.message))
  }

  return (
    <div>
      <h2>Reportes activos</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form onSubmit={guardar} style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap', marginBottom: '1rem' }}>
        <input placeholder="ID Mascota*" type="number" value={form.mascotaId} onChange={e => setForm({ ...form, mascotaId: e.target.value })} required />
        <input placeholder="Ubicación*" value={form.ubicacion} onChange={e => setForm({ ...form, ubicacion: e.target.value })} required />
        <select value={form.tipoReporte} onChange={e => setForm({ ...form, tipoReporte: e.target.value })}>
          <option value="PERDIDA">Pérdida</option>
          <option value="HALLAZGO">Hallazgo</option>
        </select>
        <input placeholder="Descripción" value={form.descripcion} onChange={e => setForm({ ...form, descripcion: e.target.value })} />
        <button type="submit">Crear reporte</button>
      </form>
      <table border="1" cellPadding="8" style={{ borderCollapse: 'collapse' }}>
        <thead>
          <tr><th>ID</th><th>Mascota</th><th>Ubicación</th><th>Tipo</th><th>Estado</th><th>Fecha</th><th>Acciones</th></tr>
        </thead>
        <tbody>
          {reportes.map(r => (
            <tr key={r.id}>
              <td>{r.id}</td><td>{r.mascotaId}</td><td>{r.ubicacion}</td>
              <td>{r.tipoReporte}</td><td>{r.estado}</td><td>{r.fechaRegistro}</td>
              <td>
                <button onClick={() => cambiarEstado(r.id, 'FOUND')}>Encontrada</button>{' '}
                <button onClick={() => cambiarEstado(r.id, 'CLOSED')}>Cerrar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default ReportesPage
