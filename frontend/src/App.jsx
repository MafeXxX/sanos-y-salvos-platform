import { Routes, Route, Link } from 'react-router-dom'
import MascotasPage from './pages/MascotasPage'
import UsuariosPage from './pages/UsuariosPage'
import ReportesPage from './pages/ReportesPage'

function App({ keycloak }) {
  return (
    <div>
      <nav className="navbar">
        <span className="navbar-brand">🐾 Sanos y Salvos</span>
        <Link to="/">Mascotas</Link>
        <Link to="/usuarios">Usuarios</Link>
        <Link to="/reportes">Reportes</Link>
        <span className="navbar-user">
          {keycloak.tokenParsed?.preferred_username}
          <button className="btn btn-outline btn-sm" onClick={() => keycloak.logout()}>
            Cerrar sesión
          </button>
        </span>
      </nav>
      <div className="container">
        <Routes>
          <Route path="/" element={<MascotasPage />} />
          <Route path="/usuarios" element={<UsuariosPage />} />
          <Route path="/reportes" element={<ReportesPage />} />
        </Routes>
      </div>
    </div>
  )
}

export default App
