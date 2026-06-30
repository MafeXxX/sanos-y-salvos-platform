package cl.duoc.sanosysalvos.mascotas.service;

import cl.duoc.sanosysalvos.mascotas.facade.UsuarioServiceFacade;
import cl.duoc.sanosysalvos.mascotas.model.Mascota;
import cl.duoc.sanosysalvos.mascotas.repository.MascotaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private UsuarioServiceFacade usuarioServiceFacade;

    @InjectMocks
    private MascotaService mascotaService;

    // ── listarTodas ─────────────────────────────────────────────────────────

    @Test
    void listarTodasDeberiaRetornarLista() {
        Mascota mascota = new Mascota();
        when(mascotaRepository.findAll()).thenReturn(List.of(mascota));

        List<Mascota> resultado = mascotaService.listarTodas();

        assertEquals(1, resultado.size());
        verify(mascotaRepository).findAll();
    }

    // ── buscarPorId ─────────────────────────────────────────────────────────

    @Test
    void buscarPorIdExistenteDeberiaRetornarMascota() {
        Mascota mascota = new Mascota();
        mascota.setId(1L);
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));

        Mascota resultado = mascotaService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarPorIdInexistenteDeberiaLanzarExcepcion() {
        when(mascotaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> mascotaService.buscarPorId(99L));
    }

    // ── registrar ───────────────────────────────────────────────────────────

    @Test
    void registrarMascotaDeberiaValidarUsuarioYGuardar() {
        UsuarioServiceFacade.UsuarioDTO usuarioDTO = new UsuarioServiceFacade.UsuarioDTO();
        usuarioDTO.setId(1L);

        Mascota datos = new Mascota();
        datos.setNombre("Luna");
        datos.setEspecie("Gato");
        datos.setUsuarioId(1L);

        when(usuarioServiceFacade.getUsuario(1L)).thenReturn(usuarioDTO);
        when(mascotaRepository.save(any(Mascota.class))).thenAnswer(inv -> inv.getArgument(0));

        Mascota resultado = mascotaService.registrar(datos);

        assertNotNull(resultado);
        assertEquals("Luna", resultado.getNombre());
        verify(usuarioServiceFacade).getUsuario(1L);
        verify(mascotaRepository).save(any(Mascota.class));
    }

    // ── actualizar ──────────────────────────────────────────────────────────

    @Test
    void actualizarMascotaDeberiaModificarCampos() {
        Mascota existente = new Mascota();
        existente.setId(1L);
        existente.setNombre("Viejo");

        Mascota nuevosDatos = new Mascota();
        nuevosDatos.setNombre("Nuevo");
        nuevosDatos.setEspecie("Perro");
        nuevosDatos.setRaza("Labrador");
        nuevosDatos.setColor("Negro");
        nuevosDatos.setEdad(5);

        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(mascotaRepository.save(any(Mascota.class))).thenAnswer(inv -> inv.getArgument(0));

        Mascota resultado = mascotaService.actualizar(1L, nuevosDatos);

        assertEquals("Nuevo", resultado.getNombre());
        assertEquals("Perro", resultado.getEspecie());
        assertEquals("Labrador", resultado.getRaza());
    }

    // ── buscarPorUsuario ────────────────────────────────────────────────────

    @Test
    void buscarPorUsuarioDeberiaRetornarMascotas() {
        Mascota mascota = new Mascota();
        when(mascotaRepository.findByUsuarioId(1L)).thenReturn(List.of(mascota));

        List<Mascota> resultado = mascotaService.buscarPorUsuario(1L);

        assertEquals(1, resultado.size());
    }

    // ── actualizarReporteActivo ─────────────────────────────────────────────

    @Test
    void actualizarReporteActivoDeberiaGuardarFlag() {
        Mascota mascota = new Mascota();
        mascota.setId(1L);
        mascota.setTieneReporteActivo(false);
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));
        when(mascotaRepository.save(any(Mascota.class))).thenAnswer(inv -> inv.getArgument(0));

        mascotaService.actualizarReporteActivo(1L, true);

        ArgumentCaptor<Mascota> captor = ArgumentCaptor.forClass(Mascota.class);
        verify(mascotaRepository).save(captor.capture());
        assertTrue(captor.getValue().isTieneReporteActivo());
    }

    // ── eliminar ────────────────────────────────────────────────────────────

    @Test
    void eliminarMascotaDeberiaLlamarDelete() {
        Mascota mascota = new Mascota();
        mascota.setId(1L);
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));

        mascotaService.eliminar(1L);

        verify(mascotaRepository).delete(mascota);
    }
}
