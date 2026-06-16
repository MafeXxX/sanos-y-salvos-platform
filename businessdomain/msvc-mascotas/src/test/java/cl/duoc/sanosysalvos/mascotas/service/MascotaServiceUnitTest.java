package cl.duoc.sanosysalvos.mascotas.service;

import cl.duoc.sanosysalvos.mascotas.model.Mascota;
import cl.duoc.sanosysalvos.mascotas.repository.MascotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MascotaServiceUnitTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @InjectMocks
    private MascotaService mascotaService;

    private Mascota mascotaPrueba;

    @BeforeEach
    void setUp() {
        mascotaPrueba = new Mascota();
        mascotaPrueba.setNombre("Firulais");
        mascotaPrueba.setEspecie("Perro");
    }

    @Test
    @DisplayName("Debería guardar una mascota correctamente")
    void deberiaGuardarMascota() {
        Mascota mascotaGuardada = new Mascota();
        mascotaGuardada.setId(1L);
        mascotaGuardada.setNombre("Firulais");
        mascotaGuardada.setEspecie("Perro");

        // Usamos el repositorio simulado
        Mockito.when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascotaGuardada);

        // Llamamos al método real de tu servicio: crearMascota
        Mascota resultado = mascotaService.crearMascota(mascotaPrueba);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Firulais", resultado.getNombre());
        Mockito.verify(mascotaRepository, Mockito.times(1)).save(any(Mascota.class));
    }

    @Test
    @DisplayName("Debería retornar la lista de todas las mascotas")
    void deberiaListarTodasLasMascotas() {
        Mockito.when(mascotaRepository.findAll()).thenReturn(Arrays.asList(mascotaPrueba));

        List<Mascota> resultado = mascotaService.listarTodas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Firulais", resultado.get(0).getNombre());
    }
}