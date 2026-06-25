package cl.duoc.sanosysalvos.bff.client;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "msvc-reportes", url = "${msvc.reportes.url:http://localhost:8082}")
public interface ReporteClient {

    @GetMapping("/api/reportes")
    List<ReporteDTO> listarActivos();

    @GetMapping("/api/reportes/mascota/{mascotaId}")
    List<ReporteDTO> buscarPorMascota(@PathVariable Long mascotaId);

    @Data
    class ReporteDTO {
        private Long id;
        private Long mascotaId;
        private String ubicacion;
        private LocalDate fechaRegistro;
        private String estado;
        private String descripcion;
        private String tipoReporte;
    }
}
