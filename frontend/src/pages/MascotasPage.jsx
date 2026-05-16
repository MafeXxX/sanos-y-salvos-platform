import { useEffect, useState } from 'react'
import { mascotasApi } from '../services/api'
import MascotaForm from '../components/MascotaForm'

function MascotasPage() {
  const [mascotas, setMascotas] = useState([])
  const [error, setError] = useState(null)
  const [mostrarForm, setMostrarForm] = useState(false)

  const cargar = () => {
    mascotasApi.listar()
      .then(r => setMascotas(r.data))
      .catch(e => setError(e.message))
  }

  useEffect(() => { cargar() }, [])

  return (
    <div>
      <h2>Mascotas registradas</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <button onClick={() => setMostrarForm(!mostrarForm)}>
        {mostrarForm ? 'Cancelar' : '+ Nueva mascota'}
      </button>
      {mostrarForm && <MascotaForm onGuardado={() => { setMostrarForm(false); cargar() }} />}
      <table border="1" cellPadding="8" style={{ marginTop: '1rem', borderCollapse: 'collapse' }}>
        <thead>
          <tr><th>ID</th><th>Nombre</th><th>Especie</th><th>Raza</th><th>Color</th><th>Edad</th><th>Reporte activo</th></tr>
        </thead>
        <tbody>
          {mascotas.map(m => (
            <tr key={m.id}>
              <td>{m.id}</td>
              <td>{m.nombre}</td>
              <td>{m.especie}</td>
              <td>{m.raza || '-'}</td>
              <td>{m.color || '-'}</td>
              <td>{m.edad}</td>
              <td>{m.tieneReporteActivo ? 'Sí' : 'No'}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

export default MascotasPage
