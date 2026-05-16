import Keycloak from 'keycloak-js'

const keycloak = new Keycloak({
  url: 'http://localhost:9090',
  realm: 'sanosysalvos',
  clientId: 'sanos-y-salvos-client'
})

export default keycloak