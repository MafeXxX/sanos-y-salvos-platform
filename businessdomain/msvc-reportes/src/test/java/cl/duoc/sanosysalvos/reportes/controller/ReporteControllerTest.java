package cl.duoc.sanosysalvos.reportes.controller;

import cl.duoc.sanosysalvos.reportes.model.Reporte;
import cl.duoc.sanosysalvos.reportes.model.ReporteEstado;
import cl.duoc.sanosysalvos.reportes.service.ReporteService;
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

@WebMvcTest(value = ReporteController.class, properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false"
})
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteService reporteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarActivosDeberiaRetornar200() throws Exception {
        when(reporteService.listarActivos()).thenReturn(List.of());

        mockMvc.perform(get("/api/reportes"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorIdDeberiaRetornar200() throws Exception {
        Reporte reporte = new Reporte();
        reporte.setId(1L);
        when(reporteService.buscarPorId(1L)).thenReturn(reporte);

        mockMvc.perform(get("/api/reportes/1"))
                .andExpect(status().isOk());
    }

    @Test
    void crearDeberiaRetornar201() throws Exception {
        Reporte reporte = new Reporte();
        reporte.setMascotaId(1L);
        reporte.setUbicacion("Plaza");
        when(reporteService.crear(any(Reporte.class))).thenReturn(reporte);

        mockMvc.perform(post("/api/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reporte)))
                .andExpect(status().isCreated());
    }

    @Test
    void cambiarEstadoDeberiaRetornar200() throws Exception {
        Reporte reporte = new Reporte();
        when(reporteService.cambiarEstado(1L, ReporteEstado.FOUND)).thenReturn(reporte);

        mockMvc.perform(put("/api/reportes/1/estado?estado=FOUND"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorMascotaDeberiaRetornar200() throws Exception {
        when(reporteService.buscarPorMascota(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/reportes/mascota/1"))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarDeberiaRetornar204() throws Exception {
        doNothing().when(reporteService).eliminar(1L);

        mockMvc.perform(delete("/api/reportes/1"))
                .andExpect(status().isNoContent());
    }
}
