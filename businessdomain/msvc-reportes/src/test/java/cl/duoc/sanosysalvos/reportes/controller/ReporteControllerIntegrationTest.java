package cl.duoc.sanosysalvos.reportes.controller;

import cl.duoc.sanosysalvos.reportes.model.Reporte;
import cl.duoc.sanosysalvos.reportes.model.ReporteEstado;
import cl.duoc.sanosysalvos.reportes.service.ReporteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ReporteController.class)
public class ReporteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteService reporteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Integración: Debería crear un reporte exitosamente y responder HTTP 201")
    void deberiaCrearReporte() throws Exception {
        // 1. Preparamos el objeto que enviaremos en el body del POST
        Reporte reporteEntrada = new Reporte();
        reporteEntrada.setMascotaId(10L);
        reporteEntrada.setUbicacion("Parque Central, Puente Alto");
        reporteEntrada.setDescripcion("Perrito visto cerca de los juegos");
        reporteEntrada.setTipoReporte("VISTO");

        // 2. Preparamos la respuesta que nos daría el servicio interno
        Reporte reporteGuardado = new Reporte();
        reporteGuardado.setId(1L);
        reporteGuardado.setMascotaId(10L);
        reporteGuardado.setUbicacion("Parque Central, Puente Alto");
        reporteGuardado.setDescripcion("Perrito visto cerca de los juegos");
        reporteGuardado.setTipoReporte("VISTO");
        reporteGuardado.setEstado(ReporteEstado.ACTIVE);
        reporteGuardado.setFechaRegistro(LocalDate.now());

        Mockito.when(reporteService.crear(Mockito.any(Reporte.class))).thenReturn(reporteGuardado);

        // 3. Ejecutamos la petición POST simulada hacia el controlador
        mockMvc.perform(post("/api/reportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reporteEntrada)))
                .andExpect(status().isCreated()) // Esperamos HTTP 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ubicacion").value("Parque Central, Puente Alto"))
                .andExpect(jsonPath("$.estado").value("ACTIVE"));
    }

    @Test
    @DisplayName("Integración: Debería retornar HTTP 400 cuando falta la ubicación")
    void deberiaRetornarBadRequestCuandoFaltaUbicacion() throws Exception {
        Reporte reporteInvalido = new Reporte();
        reporteInvalido.setMascotaId(10L);
        // No le asignamos ubicación, lo que debería disparar el IllegalArgumentException en el service

        Mockito.when(reporteService.crear(Mockito.any(Reporte.class)))
                .thenThrow(new IllegalArgumentException("La ubicación es obligatoria"));

        mockMvc.perform(post("/api/reportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reporteInvalido)))
                .andExpect(status().isBadRequest()); // El ExceptionHandler de tu controlador atrapa el error y lanza 400
    }
}