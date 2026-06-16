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
}