package cl.duoc.sanosysalvos.bff.controller;

import cl.duoc.sanosysalvos.bff.facade.SanosYSalvosFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bff")
@RequiredArgsConstructor
public class BffController {

    private final SanosYSalvosFacade facade;

    @GetMapping("/mascotas-con-reportes")
    public ResponseEntity<List<SanosYSalvosFacade.MascotaConReportesDTO>> getMascotasConReportes() {
        return ResponseEntity.ok(facade.getMascotasConReportes());
    }

    @GetMapping("/reportes-activos")
    public ResponseEntity<List<SanosYSalvosFacade.ReporteConDetalleDTO>> getReportesActivos() {
        return ResponseEntity.ok(facade.getReportesActivos());
    }

    @GetMapping("/mascota/{id}/propietario")
    public ResponseEntity<SanosYSalvosFacade.MascotaConPropietarioDTO> getMascotaConPropietario(@PathVariable Long id) {
        return ResponseEntity.ok(facade.getMascotaConPropietario(id));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleError(Exception ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}
