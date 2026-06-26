package cl.duoc.sanosysalvos.usuarios;

import cl.duoc.sanosysalvos.usuarios.adapter.MascotaClientAdapter;
import cl.duoc.sanosysalvos.usuarios.model.Usuario;
import cl.duoc.sanosysalvos.usuarios.repository.UsuarioRepository;
import cl.duoc.sanosysalvos.usuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb_usuarios_int;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "eureka.client.enabled=false",
        "spring.cloud.config.enabled=false",
        "spring.cloud.config.import-check.enabled=false",
        "spring.cloud.bootstrap.enabled=false",
        "msvc.mascotas.url=http://localhost:9999"
})
class UsuarioIntegrationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @MockBean
    private MascotaClientAdapter mascotaClientAdapter;

    @BeforeEach
    void limpiar() {
        usuarioRepository.deleteAll();
    }

    @Test
    void registrarUsuario_debePersistitConId() {
        Usuario nuevo = Usuario.builder()
                .nombre("María González")
                .email("maria@test.com")
                .telefono("+56912345678")
                .direccion("Av. Principal 123")
                .build();

        Usuario guardado = usuarioService.registrar(nuevo);

        assertNotNull(guardado.getId());
        assertEquals("María González", guardado.getNombre());
        assertEquals("maria@test.com", guardado.getEmail());
    }

    @Test
    void registrarEmailDuplicado_noDebePersistitYLanzaExcepcion() {
        usuarioService.registrar(Usuario.builder().nombre("Uno").email("dup@test.com").build());

        assertThrows(IllegalArgumentException.class, () ->
                usuarioService.registrar(Usuario.builder().nombre("Dos").email("dup@test.com").build())
        );

        assertEquals(1, usuarioRepository.count());
    }

    @Test
    void listarTodos_debeRetornarTodosLosUsuarios() {
        usuarioService.registrar(Usuario.builder().nombre("Ana").email("ana@test.com").build());
        usuarioService.registrar(Usuario.builder().nombre("Luis").email("luis@test.com").build());

        List<Usuario> todos = usuarioService.listarTodos();
        assertEquals(2, todos.size());
    }

    @Test
    void actualizarUsuario_debePersistirCambios() {
        Usuario registrado = usuarioService.registrar(
                Usuario.builder().nombre("Original").email("orig@test.com").build());

        Usuario cambios = Usuario.builder()
                .nombre("Actualizado")
                .email("nuevo@test.com")
                .telefono("999")
                .build();

        usuarioService.actualizar(registrado.getId(), cambios);

        Usuario desdeBD = usuarioRepository.findById(registrado.getId()).orElseThrow();
        assertEquals("Actualizado", desdeBD.getNombre());
        assertEquals("nuevo@test.com", desdeBD.getEmail());
    }

}