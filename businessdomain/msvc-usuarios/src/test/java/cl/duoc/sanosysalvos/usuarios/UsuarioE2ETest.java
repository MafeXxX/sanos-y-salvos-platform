package cl.duoc.sanosysalvos.usuarios;

import cl.duoc.sanosysalvos.usuarios.adapter.MascotaClientAdapter;
import cl.duoc.sanosysalvos.usuarios.model.Usuario;
import cl.duoc.sanosysalvos.usuarios.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba END-TO-END (E2E) — msvc-usuarios
 *
 * Qué prueba: flujos HTTP completos → Controller → Service → BD.
 *
 * Proceso de negocio crítico cubierto:
 *   "Registrar un dueño y consultar sus mascotas"
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb_usuarios;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "eureka.client.enabled=false",
        "spring.cloud.config.enabled=false",
        "spring.cloud.config.import-check.enabled=false"
})
class UsuarioE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // MascotaClientAdapter llama a msvc-mascotas (otro servicio) → mockeado
    @MockBean
    private MascotaClientAdapter mascotaClientAdapter;

    @BeforeEach
    void limpiar() {
        usuarioRepository.deleteAll();
    }

    // -------------------------------------------------------------------
    // FLUJO E2E 1: Registrar usuario (POST) → Obtenerlo por ID (GET)
    // Proceso crítico: un nuevo dueño se registra en la plataforma
    // -------------------------------------------------------------------
    @Test
    void flujo_registrarYBuscarUsuario() throws Exception {
        // PASO 1: Registrar via HTTP POST
        Usuario nuevo = Usuario.builder()
                .nombre("Juan Pérez")
                .email("juan@test.com")
                .telefono("+56912345678")
                .direccion("Calle Falsa 123")
                .build();

        MvcResult result = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.email").value("juan@test.com"))
                .andReturn();

        // PASO 2: Obtener por ID (verifica que persiste correctamente)
        Long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
    }

    // -------------------------------------------------------------------
    // FLUJO E2E 2: Email duplicado → sistema rechaza el segundo registro
    // Proceso crítico: integridad de datos de usuarios
    // -------------------------------------------------------------------
    @Test
    void flujo_emailDuplicado_debeRechazarSegundoRegistro() throws Exception {
        Usuario primero = Usuario.builder().nombre("Primero").email("unico@test.com").build();
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(primero)))
                .andExpect(status().isCreated());

        // Intentar registrar con el mismo email
        Usuario segundo = Usuario.builder().nombre("Segundo").email("unico@test.com").build();
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(segundo)))
                .andExpect(status().is4xxClientError());
    }

    // -------------------------------------------------------------------
    // FLUJO E2E 3: Registrar → Actualizar → Verificar cambios
    // Proceso crítico: dueño actualiza sus datos de contacto
    // -------------------------------------------------------------------
    @Test
    void flujo_actualizarDatosDeUsuario() throws Exception {
        // Registrar
        Usuario u = Usuario.builder().nombre("Antiguo").email("ant@test.com").build();
        MvcResult result = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isCreated())
                .andReturn();

        Long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        // Actualizar
        Usuario actualizado = Usuario.builder()
                .nombre("Nuevo Nombre")
                .email("nuevo@test.com")
                .telefono("+56999999999")
                .direccion("Nueva Dirección 456")
                .build();

        mockMvc.perform(put("/api/usuarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nuevo Nombre"))
                .andExpect(jsonPath("$.telefono").value("+56999999999"));
    }

    // -------------------------------------------------------------------
    // FLUJO E2E 4: Consultar mascotas de un usuario (comunicación entre servicios)
    // Proceso crítico: dueño consulta sus mascotas registradas
    // -------------------------------------------------------------------
    @Test
    void flujo_consultarMascotasDeUsuario() throws Exception {
        // Registrar usuario
        Usuario u = Usuario.builder().nombre("Dueño").email("dueno@test.com").build();
        MvcResult result = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isCreated())
                .andReturn();

        Long usuarioId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        // Simular respuesta del msvc-mascotas (comunicación entre microservicios)
        MascotaClientAdapter.MascotaDTO mascota = new MascotaClientAdapter.MascotaDTO();
        mascota.setId(1L);
        mascota.setNombre("Rex");
        mascota.setEspecie("Perro");
        when(mascotaClientAdapter.getMascotasByUsuario(usuarioId)).thenReturn(List.of(mascota));

        // Consultar mascotas del usuario via BFF
        mockMvc.perform(get("/api/usuarios/{id}/mascotas", usuarioId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Rex"));
    }

    // -------------------------------------------------------------------
    // FLUJO E2E 5: Listar todos los usuarios
    // -------------------------------------------------------------------
    @Test
    void flujo_listarTodosLosUsuarios() throws Exception {
        for (int i = 1; i <= 3; i++) {
            Usuario u = Usuario.builder().nombre("Usuario " + i).email("user" + i + "@test.com").build();
            mockMvc.perform(post("/api/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(u)))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
}
