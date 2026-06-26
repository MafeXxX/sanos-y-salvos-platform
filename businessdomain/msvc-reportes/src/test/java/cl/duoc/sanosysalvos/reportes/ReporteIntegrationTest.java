package cl.duoc.sanosysalvos.reportes;

import cl.duoc.sanosysalvos.reportes.facade.MascotaServiceFacade;
import cl.duoc.sanosysalvos.reportes.model.Reporte;
import cl.duoc.sanosysalvos.reportes.model.ReporteEstado;
import cl.duoc.sanosysalvos.reportes.repository.ReporteRepository;
import cl.duoc.sanosysalvos.reportes.service.ReporteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb_reportes_int;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "eureka.client.enabled=false",
        "spring.cloud.config.enabled=false",
        "spring.cloud.config.import-check.enabled=false",
        "spring.cloud.bootstrap.enabled=false",
        "msvc.mascotas.url=http://localhost:9999"
})
class ReporteIntegrationTest {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ReporteRepository reporteRepository;

    @MockBean
    private MascotaServiceFacade mascotaServiceFacade;

    @BeforeEach
    void preparar() {
        reporteRepository.deleteAll();
        MascotaServiceFacade.MascotaDTO mascotaDTO = new MascotaServiceFacade.MascotaDTO();
        mascotaDTO.setId(1L);
        mascotaDTO.setNombre("Rex");
        when(mascotaServiceFacade.getMascota(anyLong())).thenReturn(mascotaDTO);
        doNothing().when(mascotaServiceFacade).actualizarReporteActivo(anyLong(), anyBoolean());
    }

    @Test
    void crearReporte_debePersistitConEstadoActiveYFechaHoy() {
        Reporte datos = Reporte.builder()
                .mascotaId(1L)
                .ubicacion("Plaza de Armas, Santiago")
                .descripcion("Perro negro sin collar")
                .tipoReporte("PERDIDO")
                .build();

        Reporte creado = reporteService.crear(datos);

        assertNotNull(creado.getId());
        assertEquals(ReporteEstado.ACTIVE, creado.getEstado());
        assertNotNull(creado.getFechaRegistro());
        assertEquals("Plaza de Armas, Santiago", creado.getUbicacion());
    }

    @Test
    void cicloDeVidaCompleto_ACTIVE_FOUND_CLOSED() {
        Reporte reporte = reporteService.crear(Reporte.builder()
                .mascotaId(1L)
                .ubicacion("Parque O'Higgins")
                .tipoReporte("PERDIDO")
                .build());

        assertEquals(ReporteEstado.ACTIVE, reporte.getEstado());

        Reporte encontrado = reporteService.cambiarEstado(reporte.getId(), ReporteEstado.FOUND);
        assertEquals(ReporteEstado.FOUND, encontrado.getEstado());

        Reporte desdeBD = reporteRepository.findById(reporte.getId()).orElseThrow();
        assertEquals(ReporteEstado.FOUND, desdeBD.getEstado());

        Reporte cerrado = reporteService.cambiarEstado(reporte.getId(), ReporteEstado.CLOSED);
        assertEquals(ReporteEstado.CLOSED, cerrado.getEstado());
    }

    @Test
    void listarActivos_debeRetornarSoloReportesActive() {
        Reporte r1 = reporteService.crear(Reporte.builder()
                .mascotaId(1L).ubicacion("Loc 1").tipoReporte("PERDIDO").build());
        Reporte r2 = reporteService.crear(Reporte.builder()
                .mascotaId(1L).ubicacion("Loc 2").tipoReporte("PERDIDO").build());

        reporteService.cambiarEstado(r2.getId(), ReporteEstado.CLOSED);

        List<Reporte> activos = reporteService.listarActivos();
        assertEquals(1, activos.size());
        assertEquals(ReporteEstado.ACTIVE, activos.get(0).getEstado());
    }

    @Test
    void buscarPorMascota_debeRetornarHistorialCompleto() {
        reporteService.crear(Reporte.builder().mascotaId(1L).ubicacion("A").tipoReporte("P").build());
        reporteService.crear(Reporte.builder().mascotaId(1L).ubicacion("B").tipoReporte("P").build());
        reporteService.crear(Reporte.builder().mascotaId(2L).ubicacion("C").tipoReporte("P").build());

        List<Reporte> historial = reporteService.buscarPorMascota(1L);
        assertEquals(2, historial.size());
        assertTrue(historial.stream().allMatch(r -> r.getMascotaId().equals(1L)));
    }

    @Test
    void crearSinUbicacion_noDebePersistitYLanzaExcepcion() {
        Reporte sinUbicacion = Reporte.builder().mascotaId(1L).ubicacion("").build();
        assertThrows(IllegalArgumentException.class, () -> reporteService.crear(sinUbicacion));
        assertEquals(0, reporteRepository.count());
    }
}