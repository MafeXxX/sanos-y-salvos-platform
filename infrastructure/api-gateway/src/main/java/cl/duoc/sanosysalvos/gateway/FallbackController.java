package cl.duoc.sanosysalvos.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FallbackController {

    @GetMapping("/fallback/bff")
    public ResponseEntity<Map<String, String>> fallbackBff() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "BFF no disponible temporalmente", "componente", "bff"));
    }

    @GetMapping("/fallback/mascotas")
    public ResponseEntity<Map<String, String>> fallbackMascotas() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Microservicio de mascotas no disponible", "componente", "msvc-mascotas"));
    }

    @GetMapping("/fallback/usuarios")
    public ResponseEntity<Map<String, String>> fallbackUsuarios() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Microservicio de usuarios no disponible", "componente", "msvc-usuarios"));
    }

    @GetMapping("/fallback/reportes")
    public ResponseEntity<Map<String, String>> fallbackReportes() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Microservicio de reportes no disponible", "componente", "msvc-reportes"));
    }
}
