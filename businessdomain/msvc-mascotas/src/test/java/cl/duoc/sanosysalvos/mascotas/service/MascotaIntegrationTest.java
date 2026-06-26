package cl.duoc.sanosysalvos.mascotas.service;

import cl.duoc.sanosysalvos.mascotas.model.Mascota;
import cl.duoc.sanosysalvos.mascotas.repository.MascotaRepository;
import cl.duoc.sanosysalvos.mascotas.service.MascotaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb_mascotas_int;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "eureka.client.enabled=false",
        "spring.cloud.config.enabled=false",
        "spring.cloud.config.import-check.enabled=false",
        "spring.cloud.bootstrap.enabled=false"
})
class MascotaIntegrationTest {

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private MascotaRepository mascotaRepository;

    @BeforeEach
    void limpiarBD() {
        mascotaRepository.deleteAll();
    }

    @Test
    void registrarMascota_debePersistitEnBD() {
        Mascota datos = Mascota.builder()
                .nombre("Firulais").especie("Perro").raza("Labrador")
                .color("Negro").edad(3).usuarioId(1L).build();

        Mascota guardada = mascotaService.registrar(datos);

        assertNotNull(guardada.getId());
        assertEquals("Firulais", guardada.getNombre());
        assertFalse(guardada.isTieneReporteActivo());
    }

    @Test
    void buscarPorUsuario_debeRetornarSoloLasMascotasDelUsuario() {
        mascotaService.registrar(Mascota.builder().nombre("Rex").especie("Perro").usuarioId(1L).build());
        mascotaService.registrar(Mascota.builder().nombre("Misi").especie("Gato").usuarioId(1L).build());
        mascotaService.registrar(Mascota.builder().nombre("Otro").especie("Ave").usuarioId(2L).build());

        List<Mascota> mascotasUsuario1 = mascotaService.buscarPorUsuario(1L);

        assertEquals(2, mascotasUsuario1.size());
        assertTrue(mascotasUsuario1.stream().allMatch(m -> m.getUsuarioId().equals(1L)));
    }

    @Test
    void actualizarMascota_debePersistirCambiosEnBD() {
        Mascota original = mascotaService.registrar(
                Mascota.builder().nombre("Viejo").especie("Perro").usuarioId(1L).build());

        Mascota cambios = Mascota.builder()
                .nombre("Nuevo").especie("Perro").raza("Mestizo").color("Blanco").edad(5).build();

        mascotaService.actualizar(original.getId(), cambios);

        Mascota desdeBD = mascotaRepository.findById(original.getId()).orElseThrow();
        assertEquals("Nuevo", desdeBD.getNombre());
    }



    @Test
    void actualizarReporteActivo_debeCambiarFlagEnBD() {
        Mascota mascota = mascotaService.registrar(
                Mascota.builder().nombre("Luna").especie("Perro").usuarioId(1L).build());

        mascotaService.actualizarReporteActivo(mascota.getId(), true);

        Mascota desdeBD = mascotaRepository.findById(mascota.getId()).orElseThrow();
        assertTrue(desdeBD.isTieneReporteActivo());
    }

    @Test
    void buscarPorIdInexistente_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> mascotaService.buscarPorId(999L));
    }
}