package cl.duoc.sanosysalvos.mascotas;

import cl.duoc.sanosysalvos.mascotas.facade.UsuarioServiceFacade;
import cl.duoc.sanosysalvos.mascotas.repository.MascotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(properties = {
    "spring.cloud.config.enabled=false",
    "eureka.client.enabled=false",
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class MascotaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MascotaRepository mascotaRepository;

    // AQUÍ: Simulamos el cliente Feign para no depender del msvc-usuarios
    @MockBean
    private UsuarioServiceFacade usuarioServiceFacade;

    @BeforeEach
    void setUp() {
        mascotaRepository.deleteAll();
    }

    @Test
    void deberiaCrearMascotaExitosamente() throws Exception {
        // Configuramos el "simulador" para que devuelva un usuario válido cuando busquen el ID 1
        UsuarioServiceFacade.UsuarioDTO mockUsuario = new UsuarioServiceFacade.UsuarioDTO();
        mockUsuario.setId(1L);
        mockUsuario.setNombre("Dueño de Prueba");
        Mockito.when(usuarioServiceFacade.getUsuario(1L)).thenReturn(mockUsuario);

        String mascotaJson = """
            {
                "nombre": "Luna",
                "especie": "Gato",
                "raza": "Siames",
                "usuarioId": 1
            }
        """;

        mockMvc.perform(post("/api/mascotas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mascotaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Luna"));
    }
}