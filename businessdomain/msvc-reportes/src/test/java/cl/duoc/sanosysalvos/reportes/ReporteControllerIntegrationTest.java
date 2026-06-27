package cl.duoc.sanosysalvos.reportes;

import cl.duoc.sanosysalvos.reportes.facade.MascotaServiceFacade;
import cl.duoc.sanosysalvos.reportes.model.Reporte;
import cl.duoc.sanosysalvos.reportes.model.ReporteEstado;
import cl.duoc.sanosysalvos.reportes.repository.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "spring.cloud.config.enabled=false",
    "eureka.client.enabled=false",
    "spring.datasource.url=jdbc:h2:mem:reportesdb;DB_CLOSE_DELAY=-1;MODE=MySQL",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.show-sql=false"
})
@AutoConfigureMockMvc
class ReporteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReporteRepository reporteRepository;

    @MockBean
    private MascotaServiceFacade mascotaServiceFacade;

    @BeforeEach
    void setUp() {
        reporteRepository.deleteAll();
    }

    @Test
    void deberiaCrearReporteExitosamente() throws Exception {
        // Simulamos que la mascota con ID 10 existe en msvc-mascotas
        MascotaServiceFacade.MascotaDTO mockMascota = new MascotaServiceFacade.MascotaDTO();
        mockMascota.setId(10L);
        Mockito.when(mascotaServiceFacade.getMascota(10L)).thenReturn(mockMascota);

        String reporteJson = """
            {
                "mascotaId": 10,
                "ubicacion": "Plaza Central",
                "estado": "ACTIVE",
                "descripcion": "Gato perdido",
                "tipoReporte": "PERDIDA"
            }
        """;

        mockMvc.perform(post("/api/reportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reporteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mascotaId").value(10))
                .andExpect(jsonPath("$.ubicacion").value("Plaza Central"));
    }
}