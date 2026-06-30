package cl.duoc.sanosysalvos.mascotas.controller;

import cl.duoc.sanosysalvos.mascotas.builder.MascotaBuilder;
import cl.duoc.sanosysalvos.mascotas.model.Mascota;
import cl.duoc.sanosysalvos.mascotas.service.MascotaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = MascotaController.class, properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false"
})
class MascotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MascotaService mascotaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarDeberiaRetornar200() throws Exception {
        when(mascotaService.listarTodas()).thenReturn(List.of());

        mockMvc.perform(get("/api/mascotas"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorIdDeberiaRetornar200() throws Exception {
        Mascota mascota = new Mascota();
        mascota.setId(1L);
        mascota.setNombre("Firulais");
        when(mascotaService.buscarPorId(1L)).thenReturn(mascota);

        mockMvc.perform(get("/api/mascotas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Firulais"));
    }

    @Test
    void registrarDeberiaRetornar201() throws Exception {
        Mascota input = new MascotaBuilder()
                .nombre("Max")
                .especie("Perro")
                .usuarioId(1L)
                .build();
        input.setId(1L);

        when(mascotaService.registrar(any(Mascota.class))).thenReturn(input);

        mockMvc.perform(post("/api/mascotas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated());
    }

    @Test
    void actualizarDeberiaRetornar200() throws Exception {
        Mascota input = new MascotaBuilder()
                .nombre("Max")
                .especie("Perro")
                .usuarioId(1L)
                .build();

        when(mascotaService.actualizar(anyLong(), any(Mascota.class))).thenReturn(input);

        mockMvc.perform(put("/api/mascotas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarDeberiaRetornar204() throws Exception {
        doNothing().when(mascotaService).eliminar(1L);

        mockMvc.perform(delete("/api/mascotas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorUsuarioDeberiaRetornar200() throws Exception {
        when(mascotaService.buscarPorUsuario(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/mascotas/usuario/1"))
                .andExpect(status().isOk());
    }

    @Test
    void actualizarReporteActivoDeberiaRetornar200() throws Exception {
        doNothing().when(mascotaService).actualizarReporteActivo(1L, true);

        mockMvc.perform(put("/api/mascotas/1/reporte-activo?activo=true"))
                .andExpect(status().isOk());
    }
}
