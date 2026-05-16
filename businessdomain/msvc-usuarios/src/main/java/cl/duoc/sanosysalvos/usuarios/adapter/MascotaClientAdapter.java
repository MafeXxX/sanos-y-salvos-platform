package cl.duoc.sanosysalvos.usuarios.adapter;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "msvc-mascotas", url = "${msvc.mascotas.url:http://localhost:8081}")
public interface MascotaClientAdapter {

    @GetMapping("/api/mascotas/usuario/{usuarioId}")
    List<MascotaDTO> getMascotasByUsuario(@PathVariable Long usuarioId);

    @Data
    class MascotaDTO {
        private Long id;
        private String nombre;
        private String especie;
        private String raza;
        private String color;
        private int edad;
        private boolean tieneReporteActivo;
    }
}
