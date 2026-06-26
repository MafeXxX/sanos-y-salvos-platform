import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import App from './App.jsx'
import keycloak from './services/keycloak.js'
import axios from 'axios'
import './index.css'

keycloak.init({ onLoad: 'login-required', checkLoginIframe: false }).then(authenticated => {
  if (!authenticated) {
    keycloak.login()
    return
  }

  axios.interceptors.request.use(config => {
    config.headers.Authorization = `Bearer ${keycloak.token}`
    return config
  })

  setInterval(() => {
    keycloak.updateToken(60).catch(() => keycloak.logout())
  }, 30000)

  ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
      <BrowserRouter>
        <App keycloak={keycloak} />
      </BrowserRouter>
    </React.StrictMode>
  )
}).catch(() => {
  console.error('Error al inicializar Keycloak')
})
