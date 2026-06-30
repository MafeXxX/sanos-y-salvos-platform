package cl.duoc.sanosysalvos.mascotas;

import cl.duoc.sanosysalvos.mascotas.builder.MascotaBuilder;
import cl.duoc.sanosysalvos.mascotas.model.Mascota;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MascotaBuilderTest {

    @Test
    void buildConDatosValidos() {
        Mascota mascota = new MascotaBuilder()
                .nombre("Firulais")
                .especie("Perro")
                .raza("Labrador")
                .usuarioId(1L)
                .build();

        assertNotNull(mascota);
        assertEquals("Firulais", mascota.getNombre());
        assertEquals("Perro", mascota.getEspecie());
        assertFalse(mascota.isTieneReporteActivo());
    }

    @Test
    void buildSinNombreLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new MascotaBuilder()
                        .especie("Perro")
                        .usuarioId(1L)
                        .build()
        );
    }

    @Test
    void buildSinEspecieLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new MascotaBuilder()
                        .nombre("Firulais")
                        .usuarioId(1L)
                        .build()
        );
    }

    @Test
    void buildSinUsuarioIdLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new MascotaBuilder()
                        .nombre("Firulais")
                        .especie("Perro")
                        .build()
        );
    }

    @Test
    void buildConNombreVacioLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new MascotaBuilder()
                        .nombre("")
                        .especie("Perro")
                        .usuarioId(1L)
                        .build()
        );
    }

    @Test
    void buildConEspecieVaciaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new MascotaBuilder()
                        .nombre("Firulais")
                        .especie("")
                        .usuarioId(1L)
                        .build()
        );
    }

    @Test
    void buildConDatosCompletosDeberiaSetearTodosLosCampos() {
        Mascota mascota = new MascotaBuilder()
                .nombre("Firulais")
                .especie("Perro")
                .raza("Labrador")
                .color("Negro")
                .edad(3)
                .usuarioId(1L)
                .build();

        assertEquals("Firulais", mascota.getNombre());
        assertEquals("Perro", mascota.getEspecie());
        assertEquals("Labrador", mascota.getRaza());
        assertEquals("Negro", mascota.getColor());
        assertEquals(3, mascota.getEdad());
        assertEquals(1L, mascota.getUsuarioId());
        assertFalse(mascota.isTieneReporteActivo());
    }
}
