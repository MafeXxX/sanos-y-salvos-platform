package cl.duoc.sanosysalvos.reportes;

import cl.duoc.sanosysalvos.reportes.facade.MascotaServiceFacade;
import cl.duoc.sanosysalvos.reportes.model.Reporte;
import cl.duoc.sanosysalvos.reportes.repository.ReporteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb_reportes;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "eureka.client.enabled=false",
        "spring.cloud.config.enabled=false",
        "spring.cloud.bootstrap.enabled=false",
        "msvc.mascotas.url=http://localhost:9999"
})
class ReporteE2ETest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ReporteRepository reporteRepository;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private MascotaServiceFacade mascotaServiceFacade;

    @BeforeEach
    void preparar() {
        reporteRepository.deleteAll();
        MascotaServiceFacade.MascotaDTO dto = new MascotaServiceFacade.MascotaDTO();
        dto.setId(1L);
        when(mascotaServiceFacade.getMascota(anyLong())).thenReturn(dto);
        doNothing().when(mascotaServiceFacade).actualizarReporteActivo(anyLong(), anyBoolean());
    }

    @Test
    void flujo_principal_mascotaPerdidaEncontradaYCerrada() throws Exception {
        Reporte nuevo = Reporte.builder().mascotaId(1L).ubicacion("Santiago").tipoReporte("PERDIDO").build();
        MvcResult result = mockMvc.perform(post("/api/reportes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated()).andReturn();
        Long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
        
        mockMvc.perform(put("/api/reportes/{id}/estado", id).param("estado", "FOUND")).andExpect(status().isOk());
        mockMvc.perform(put("/api/reportes/{id}/estado", id).param("estado", "CLOSED")).andExpect(status().isOk());
    }

    @Test
    void flujo_reporteSinUbicacion_debeSerRechazado() throws Exception {
        Reporte sinUbicacion = Reporte.builder().mascotaId(1L).ubicacion("").tipoReporte("PERDIDO").build();
        mockMvc.perform(post("/api/reportes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(sinUbicacion)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void flujo_eliminarReporte() throws Exception {
        // 1. Crear
        Reporte r = Reporte.builder().mascotaId(1L).ubicacion("Lugar").tipoReporte("P").build();
        MvcResult result = mockMvc.perform(post("/api/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isCreated()).andReturn();
        
        Long id = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();

        // 2. Eliminar (Explicitamente configurado como DELETE)
        mockMvc.perform(delete("/api/reportes/{id}", id))
                .andExpect(status().isNoContent());

        // 3. Verificar que ya no existe (Debería retornar 404 o 500 dependiendo de tu Service)
        mockMvc.perform(get("/api/reportes/{id}", id))
                .andExpect(status().isNotFound()); 
    }
}