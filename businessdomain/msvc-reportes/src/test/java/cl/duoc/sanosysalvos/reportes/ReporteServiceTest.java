package cl.duoc.sanosysalvos.reportes;

import cl.duoc.sanosysalvos.reportes.facade.MascotaServiceFacade;
import cl.duoc.sanosysalvos.reportes.model.Reporte;
import cl.duoc.sanosysalvos.reportes.model.ReporteEstado;
import cl.duoc.sanosysalvos.reportes.repository.ReporteRepository;
import cl.duoc.sanosysalvos.reportes.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private MascotaServiceFacade mascotaServiceFacade;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    void crearSinUbicacionLanzaExcepcion() {
        Reporte reporte = Reporte.builder().mascotaId(1L).ubicacion("").build();

        assertThrows(IllegalArgumentException.class, () -> reporteService.crear(reporte));
    }

    @Test
    void crearSinMascotaExistenteLanzaExcepcion() {
        Reporte reporte = Reporte.builder().mascotaId(99L).ubicacion("Santiago Centro").build();
        when(mascotaServiceFacade.getMascota(99L)).thenThrow(new RuntimeException("Mascota no encontrada"));

        assertThrows(RuntimeException.class, () -> reporteService.crear(reporte));
    }

    @Test
    void cambioDeEstadoActivoAFoundFunciona() {
        Reporte reporte = Reporte.builder()
                .id(1L).mascotaId(1L).ubicacion("Santiago").estado(ReporteEstado.ACTIVE).build();

        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(reporteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(mascotaServiceFacade).actualizarReporteActivo(1L, false);

        Reporte resultado = reporteService.cambiarEstado(1L, ReporteEstado.FOUND);

        assertEquals(ReporteEstado.FOUND, resultado.getEstado());
    }

    @Test
    void cambioDeEstadoFoundAClosedFunciona() {
        Reporte reporte = Reporte.builder()
                .id(1L).mascotaId(1L).ubicacion("Santiago").estado(ReporteEstado.FOUND).build();

        when(reporteRepository.findById(1L)).thenReturn(Optional.of(reporte));
        when(reporteRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(mascotaServiceFacade).actualizarReporteActivo(1L, false);

        Reporte resultado = reporteService.cambiarEstado(1L, ReporteEstado.CLOSED);

        assertEquals(ReporteEstado.CLOSED, resultado.getEstado());
    }
}
