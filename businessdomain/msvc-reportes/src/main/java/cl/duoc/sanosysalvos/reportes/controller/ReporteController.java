package cl.duoc.sanosysalvos.reportes.controller;

import cl.duoc.sanosysalvos.reportes.model.Reporte;
import cl.duoc.sanosysalvos.reportes.model.ReporteEstado;
import cl.duoc.sanosysalvos.reportes.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<List<Reporte>> listarActivos() {
        return ResponseEntity.ok(reporteService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Reporte> crear(@RequestBody Reporte reporte) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.crear(reporte));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Reporte> cambiarEstado(@PathVariable Long id, @RequestParam ReporteEstado estado) {
        return ResponseEntity.ok(reporteService.cambiarEstado(id, estado));
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<Reporte>> buscarPorMascota(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(reporteService.buscarPorMascota(mascotaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
