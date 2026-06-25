package cl.duoc.sanosysalvos.bff.client;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-usuarios", url = "${msvc.usuarios.url:http://localhost:8083}")
public interface UsuarioClient {

    @GetMapping("/api/usuarios/{id}")
    UsuarioDTO buscarPorId(@PathVariable Long id);

    @Data
    class UsuarioDTO {
        private Long id;
        private String nombre;
        private String email;
        private String telefono;
        private String direccion;
    }
}
