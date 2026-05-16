package cl.duoc.sanosysalvos.bff.facade;

import cl.duoc.sanosysalvos.bff.client.MascotaClient;
import cl.duoc.sanosysalvos.bff.client.ReporteClient;
import cl.duoc.sanosysalvos.bff.client.UsuarioClient;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SanosYSalvosFacade {

    private final MascotaClient mascotaClient;
    private final UsuarioClient usuarioClient;
    private final ReporteClient reporteClient;

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

    public MascotaConPropietarioDTO getMascotaConPropietario(Long mascotaId) {
        MascotaClient.MascotaDTO mascota = mascotaClient.buscarPorId(mascotaId);
        MascotaConPropietarioDTO dto = new MascotaConPropietarioDTO();
        dto.setMascota(mascota);
        dto.setPropietario(usuarioClient.buscarPorId(mascota.getUsuarioId()));
        return dto;
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
