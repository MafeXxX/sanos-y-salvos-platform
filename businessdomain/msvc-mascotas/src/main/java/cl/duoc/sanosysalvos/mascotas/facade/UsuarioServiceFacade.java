package cl.duoc.sanosysalvos.mascotas.facade;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-usuarios", url = "${msvc.usuarios.url:http://localhost:8083}", configuration = FeignErrorDecoder.class)
public interface UsuarioServiceFacade {

    @GetMapping("/api/usuarios/{id}")
    UsuarioDTO getUsuario(@PathVariable Long id);

    @Data
    class UsuarioDTO {
        private Long id;
        private String nombre;
        private String email;
        private String telefono;
        private String direccion;
    }
}
