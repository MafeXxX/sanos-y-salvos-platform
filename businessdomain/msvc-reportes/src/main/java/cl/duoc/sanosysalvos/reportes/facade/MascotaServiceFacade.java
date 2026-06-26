package cl.duoc.sanosysalvos.reportes.facade;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "msvc-mascotas", url = "${msvc.mascotas.url:http://localhost:8081}", configuration = FeignErrorDecoder.class)
public interface MascotaServiceFacade {

    @GetMapping("/api/mascotas/{id}")
    MascotaDTO getMascota(@PathVariable Long id);

    @PutMapping("/api/mascotas/{id}/reporte-activo")
    void actualizarReporteActivo(@PathVariable Long id, @RequestParam boolean activo);

    @Data
    class MascotaDTO {
        private Long id;
        private String nombre;
        private String especie;
        private Long usuarioId;
        private boolean tieneReporteActivo;
    }
}
