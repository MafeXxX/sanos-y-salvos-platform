import { Routes, Route, Link } from 'react-router-dom'
import MascotasPage from './pages/MascotasPage'
import UsuariosPage from './pages/UsuariosPage'
import ReportesPage from './pages/ReportesPage'

function App({ keycloak }) {
  return (
    <div>
      <nav style={{ padding: '1rem', background: '#2c3e50', color: 'white', display: 'flex', gap: '1.5rem', alignItems: 'center' }}>
        <strong>Sanos y Salvos</strong>
        <Link to="/" style={{ color: 'white' }}>Mascotas</Link>
        <Link to="/usuarios" style={{ color: 'white' }}>Usuarios</Link>
        <Link to="/reportes" style={{ color: 'white' }}>Reportes</Link>
        <span style={{ marginLeft: 'auto', fontSize: '0.9rem' }}>
          {keycloak.tokenParsed?.preferred_username}
          {' '}
          <button onClick={() => keycloak.logout()} style={{ marginLeft: '0.5rem' }}>
            Cerrar sesión
          </button>
        </span>
      </nav>
      <main style={{ padding: '1rem' }}>
        <Routes>
          <Route path="/" element={<MascotasPage />} />
          <Route path="/usuarios" element={<UsuariosPage />} />
          <Route path="/reportes" element={<ReportesPage />} />
        </Routes>
      </main>
    </div>
  )
}

export default App
