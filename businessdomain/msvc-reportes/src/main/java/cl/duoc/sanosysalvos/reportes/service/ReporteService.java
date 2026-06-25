package cl.duoc.sanosysalvos.reportes.service;

import cl.duoc.sanosysalvos.reportes.facade.MascotaServiceFacade;
import cl.duoc.sanosysalvos.reportes.model.Reporte;
import cl.duoc.sanosysalvos.reportes.model.ReporteEstado;
import cl.duoc.sanosysalvos.reportes.repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final MascotaServiceFacade mascotaServiceFacade;

    public List<Reporte> listarActivos() {
        return reporteRepository.findByEstado(ReporteEstado.ACTIVE);
    }

    public Reporte buscarPorId(Long id) {
        return reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));
    }

    public Reporte crear(Reporte datos) {
        if (datos.getUbicacion() == null || datos.getUbicacion().isBlank()) {
            throw new IllegalArgumentException("La ubicación es obligatoria");
        }
        mascotaServiceFacade.getMascota(datos.getMascotaId());

        Reporte reporte = Reporte.builder()
                .mascotaId(datos.getMascotaId())
                .ubicacion(datos.getUbicacion())
                .fechaRegistro(LocalDate.now())
                .estado(ReporteEstado.ACTIVE)
                .descripcion(datos.getDescripcion())
                .tipoReporte(datos.getTipoReporte())
                .build();

        Reporte guardado = reporteRepository.save(reporte);
        mascotaServiceFacade.actualizarReporteActivo(datos.getMascotaId(), true);
        return guardado;
    }

    public Reporte cambiarEstado(Long id, ReporteEstado nuevoEstado) {
        Reporte reporte = buscarPorId(id);
        reporte.setEstado(nuevoEstado);
        if (nuevoEstado == ReporteEstado.FOUND || nuevoEstado == ReporteEstado.CLOSED) {
            mascotaServiceFacade.actualizarReporteActivo(reporte.getMascotaId(), false);
        }
        return reporteRepository.save(reporte);
    }

    public List<Reporte> buscarPorMascota(Long mascotaId) {
        return reporteRepository.findByMascotaId(mascotaId);
    }

    public void eliminar(Long id) {
        Reporte reporte = buscarPorId(id);
        reporteRepository.delete(reporte);
    }
}
