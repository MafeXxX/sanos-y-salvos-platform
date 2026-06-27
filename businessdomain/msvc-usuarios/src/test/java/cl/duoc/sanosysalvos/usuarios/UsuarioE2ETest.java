package cl.duoc.sanosysalvos.usuarios;

import cl.duoc.sanosysalvos.usuarios.adapter.MascotaClientAdapter;
import cl.duoc.sanosysalvos.usuarios.model.Usuario;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:usuariosdb;DB_CLOSE_DELAY=-1;MODE=MySQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
    }
)
class UsuarioE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private MascotaClientAdapter mascotaClientAdapter;

    @Test
    void flujoCompletoUsuarioConMascotas() {
        // 1. Crear un usuario real vía POST
        Usuario nuevoUsuario = new Usuario(null, "Ana", "ana@example.com", "123456", "Calle Falsa 123");
        ResponseEntity<Usuario> responsePost = restTemplate.postForEntity("/api/usuarios", nuevoUsuario, Usuario.class);
        
        assertEquals(HttpStatus.CREATED, responsePost.getStatusCode());
        Long usuarioId = responsePost.getBody().getId();

        // 2. Simular que cuando este usuario consulta sus mascotas, recibe una lista vacía o con datos
        Mockito.when(mascotaClientAdapter.getMascotasByUsuario(usuarioId))
               .thenReturn(new ArrayList<MascotaClientAdapter.MascotaDTO>());

        // 3. Consultar mascotas del usuario (Endpoint que usa OpenFeign)
        ResponseEntity<List> responseMascotas = restTemplate.getForEntity("/api/usuarios/" + usuarioId + "/mascotas", List.class);
        
        // 4. Validar que la comunicación con el "adapter" (mockeado) funcionó
        assertEquals(HttpStatus.OK, responseMascotas.getStatusCode());
    }
}