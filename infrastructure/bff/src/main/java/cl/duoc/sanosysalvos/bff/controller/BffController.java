package cl.duoc.sanosysalvos.bff.controller;

import cl.duoc.sanosysalvos.bff.facade.SanosYSalvosFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bff")
@RequiredArgsConstructor
@Tag(name = "BFF", description = "Backend for Frontend — orquesta mascotas, reportes y usuarios con Circuit Breaker y Retry")
public class BffController {

    private final SanosYSalvosFacade facade;

    @Operation(summary = "Mascotas con reportes activos", description = "Lista todas las mascotas que tienen reportes activos, incluyendo sus reportes asociados. Protegido por Circuit Breaker.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping("/mascotas-con-reportes")
    public ResponseEntity<List<SanosYSalvosFacade.MascotaConReportesDTO>> getMascotasConReportes() {
        return ResponseEntity.ok(facade.getMascotasConReportes());
    }

    @Operation(summary = "Reportes activos con detalle", description = "Lista los reportes activos con datos completos de la mascota y su propietario. Protegido por Circuit Breaker.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping("/reportes-activos")
    public ResponseEntity<List<SanosYSalvosFacade.ReporteConDetalleDTO>> getReportesActivos() {
        return ResponseEntity.ok(facade.getReportesActivos());
    }

    @Operation(summary = "Mascota con propietario", description = "Obtiene los datos de una mascota junto con la información de su dueño. Protegido por Circuit Breaker.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos obtenidos"),
            @ApiResponse(responseCode = "404", description = "Mascota o usuario no encontrado")
    })
    @GetMapping("/mascota/{id}/propietario")
    public ResponseEntity<SanosYSalvosFacade.MascotaConPropietarioDTO> getMascotaConPropietario(@Parameter(description = "ID de la mascota", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(facade.getMascotaConPropietario(id));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleError(Exception ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}
