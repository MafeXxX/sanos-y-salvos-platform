package cl.duoc.sanosysalvos.usuarios;

import cl.duoc.sanosysalvos.usuarios.adapter.MascotaClientAdapter;
import cl.duoc.sanosysalvos.usuarios.model.Usuario;
import cl.duoc.sanosysalvos.usuarios.repository.UsuarioRepository;
import cl.duoc.sanosysalvos.usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MascotaClientAdapter mascotaClientAdapter;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void registrarSinNombreLanzaExcepcion() {
        Usuario usuario = new Usuario(null, null, "test@test.com", null, null);
        when(usuarioRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(usuarioRepository.save(any())).thenThrow(new IllegalArgumentException("El nombre es obligatorio"));

        assertThrows(Exception.class, () -> usuarioService.registrar(usuario));
    }

    @Test
    void registrarConEmailDuplicadoLanzaExcepcion() {
        Usuario usuario = new Usuario(null, "Juan", "duplicado@test.com", null, null);
        when(usuarioRepository.existsByEmail("duplicado@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrar(usuario));
    }

    @Test
    void buscarPorIdNoExistenteLanzaExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.buscarPorId(99L));
    }

    @Test
    void registrarUsuarioValido() {
        Usuario usuario = new Usuario(null, "Juan", "juan@test.com", null, null);
        Usuario guardado = new Usuario(1L, "Juan", "juan@test.com", null, null);
        when(usuarioRepository.existsByEmail("juan@test.com")).thenReturn(false);
        when(usuarioRepository.save(usuario)).thenReturn(guardado);

        Usuario resultado = usuarioService.registrar(usuario);

        assertNotNull(resultado.getId());
        assertEquals("Juan", resultado.getNombre());
    }
}
