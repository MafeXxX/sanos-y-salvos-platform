package cl.duoc.sanosysalvos.bff.facade;

import cl.duoc.sanosysalvos.bff.client.MascotaClient;
import cl.duoc.sanosysalvos.bff.client.ReporteClient;
import cl.duoc.sanosysalvos.bff.client.UsuarioClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SanosYSalvosFacade {

    private final MascotaClient mascotaClient;
    private final UsuarioClient usuarioClient;
    private final ReporteClient reporteClient;

    @CircuitBreaker(name = "bff", fallbackMethod = "fallbackMascotasConReportes")
    @Retry(name = "bff")
    public List<MascotaConReportesDTO> getMascotasConReportes() {
        return mascotaClient.listarTodas().stream()
                .filter(MascotaClient.MascotaDTO::isTieneReporteActivo)
                .map(m -> {
                    MascotaConReportesDTO dto = new MascotaConReportesDTO();
                    dto.setMascota(m);
                    dto.setReportes(reporteClient.buscarPorMascota(m.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "bff", fallbackMethod = "fallbackReportesActivos")
    @Retry(name = "bff")
    public List<ReporteConDetalleDTO> getReportesActivos() {
        return reporteClient.listarActivos().stream()
                .map(r -> {
                    ReporteConDetalleDTO dto = new ReporteConDetalleDTO();
                    dto.setReporte(r);
                    MascotaClient.MascotaDTO mascota = mascotaClient.buscarPorId(r.getMascotaId());
                    dto.setMascota(mascota);
                    dto.setPropietario(usuarioClient.buscarPorId(mascota.getUsuarioId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @CircuitBreaker(name = "bff", fallbackMethod = "fallbackMascotaConPropietario")
    @Retry(name = "bff")
    public MascotaConPropietarioDTO getMascotaConPropietario(Long mascotaId) {
        MascotaClient.MascotaDTO mascota = mascotaClient.buscarPorId(mascotaId);
        MascotaConPropietarioDTO dto = new MascotaConPropietarioDTO();
        dto.setMascota(mascota);
        dto.setPropietario(usuarioClient.buscarPorId(mascota.getUsuarioId()));
        return dto;
    }

    // ── Fallbacks ────────────────────────────────────────────────────────────

    private List<MascotaConReportesDTO> fallbackMascotasConReportes(Throwable t) {
        log.error("CircuitBreaker abierto — getMascotasConReportes: {}", t.getMessage());
        return Collections.emptyList();
    }

    private List<ReporteConDetalleDTO> fallbackReportesActivos(Throwable t) {
        log.error("CircuitBreaker abierto — getReportesActivos: {}", t.getMessage());
        return Collections.emptyList();
    }

    private MascotaConPropietarioDTO fallbackMascotaConPropietario(Long mascotaId, Throwable t) {
        log.error("CircuitBreaker abierto — getMascotaConPropietario({}): {}", mascotaId, t.getMessage());
        return null;
    }

    @Data
    public static class MascotaConReportesDTO {
        private MascotaClient.MascotaDTO mascota;
        private List<ReporteClient.ReporteDTO> reportes;
    }

    @Data
    public static class ReporteConDetalleDTO {
        private ReporteClient.ReporteDTO reporte;
        private MascotaClient.MascotaDTO mascota;
        private UsuarioClient.UsuarioDTO propietario;
    }

    @Data
    public static class MascotaConPropietarioDTO {
        private MascotaClient.MascotaDTO mascota;
        private UsuarioClient.UsuarioDTO propietario;
    }
}
