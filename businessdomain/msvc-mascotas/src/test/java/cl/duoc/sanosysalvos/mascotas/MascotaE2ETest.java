package cl.duoc.sanosysalvos.mascotas;

import cl.duoc.sanosysalvos.mascotas.facade.UsuarioServiceFacade;
import cl.duoc.sanosysalvos.mascotas.model.Mascota;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
    }
)
class MascotaE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    // Repetimos la simulación para la prueba E2E
    @MockBean
    private UsuarioServiceFacade usuarioServiceFacade;

    @Test
    void flujoCompletoCrearYObtenerMascota() {
        // Configuramos la simulación para el ID 2
        UsuarioServiceFacade.UsuarioDTO mockUsuario = new UsuarioServiceFacade.UsuarioDTO();
        mockUsuario.setId(2L);
        Mockito.when(usuarioServiceFacade.getUsuario(2L)).thenReturn(mockUsuario);

        // 1. Preparar los datos
        Mascota nuevaMascota = new Mascota();
        nuevaMascota.setNombre("Max");
        nuevaMascota.setEspecie("Perro");
        nuevaMascota.setUsuarioId(2L);

        // 2. Ejecutar POST
        ResponseEntity<Mascota> responsePost = restTemplate.postForEntity("/api/mascotas", nuevaMascota, Mascota.class);

        // 3. Validar
        assertEquals(HttpStatus.CREATED, responsePost.getStatusCode());
        assertNotNull(responsePost.getBody().getId());
        
        Long mascotaId = responsePost.getBody().getId();

        // 4. Ejecutar GET
        ResponseEntity<Mascota> responseGet = restTemplate.getForEntity("/api/mascotas/" + mascotaId, Mascota.class);

        // 5. Validar
        assertEquals(HttpStatus.OK, responseGet.getStatusCode());
        assertEquals("Max", responseGet.getBody().getNombre());
    }
}