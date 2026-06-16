package cl.duoc.sanosysalvos.reportes.service;

import cl.duoc.sanosysalvos.reportes.model.Reporte;
import cl.duoc.sanosysalvos.reportes.model.ReporteEstado;
import cl.duoc.sanosysalvos.reportes.repository.ReporteRepository;
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
public class ReporteServiceUnitTest {

    @Mock
    private ReporteRepository reporteRepository;

    @InjectMocks
    private ReporteService reporteService;

    private Reporte reportePrueba;

    @BeforeEach
    void setUp() {
        reportePrueba = new Reporte();
        reportePrueba.setDescripcion("Gato extraviado en paradero 14");
        reportePrueba.setEstado(ReporteEstado.PERDIDO);
    }

    @Test
    @DisplayName("Debería registrar un reporte de mascota exitosamente")
    void deberiaCrearReporte() {
        Reporte reporteGuardado = new Reporte();
        reporteGuardado.setId(10L);
        reporteGuardado.setDescripcion("Gato extraviado en paradero 14");
        reporteGuardado.setEstado(ReporteEstado.PERDIDO);

        Mockito.when(reporteRepository.save(any(Reporte.class))).thenReturn(reporteGuardado);

        Reporte resultado = reporteService.crearReporte(reportePrueba);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals(ReporteEstado.PERDIDO, resultado.getEstado());
        Mockito.verify(reporteRepository, Mockito.times(1)).save(any(Reporte.class));
    }

    @Test
    @DisplayName("Debería obtener el listado histórico de reportes")
    void deberiaListarTodosLosReportes() {
        Mockito.when(reporteRepository.findAll()).thenReturn(Arrays.asList(reportePrueba));

        List<Reporte> resultado = reporteService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Gato extraviado en paradero 14", resultado.get(0).getDescripcion());
    }
}