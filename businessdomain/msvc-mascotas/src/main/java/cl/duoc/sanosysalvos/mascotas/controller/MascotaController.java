package cl.duoc.sanosysalvos.mascotas.controller;

import cl.duoc.sanosysalvos.mascotas.model.Mascota;
import cl.duoc.sanosysalvos.mascotas.service.MascotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor
public class MascotaController {

    private final MascotaService mascotaService;

    @GetMapping
    public ResponseEntity<List<Mascota>> listar() {
        return ResponseEntity.ok(mascotaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mascota> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Mascota> registrar(@RequestBody Mascota mascota) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mascotaService.registrar(mascota));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mascota> actualizar(@PathVariable Long id, @RequestBody Mascota mascota) {
        return ResponseEntity.ok(mascotaService.actualizar(id, mascota));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Mascota>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(mascotaService.buscarPorUsuario(usuarioId));
    }

    @PutMapping("/{id}/reporte-activo")
    public ResponseEntity<Void> actualizarReporteActivo(@PathVariable Long id, @RequestParam boolean activo) {
        mascotaService.actualizarReporteActivo(id, activo);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
