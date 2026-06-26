package cl.duoc.sanosysalvos.reportes.controller;

import cl.duoc.sanosysalvos.reportes.model.Reporte;
import cl.duoc.sanosysalvos.reportes.model.ReporteEstado;
import cl.duoc.sanosysalvos.reportes.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Gestión de reportes de mascotas perdidas/halladas — utiliza Facade para consultar msvc-mascotas")
public class ReporteController {

    private final ReporteService reporteService;

    @Operation(summary = "Listar reportes activos", description = "Obtiene todos los reportes en estado ACTIVE")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Reporte>> listarActivos() {
        return ResponseEntity.ok(reporteService.listarActivos());
    }

    @Operation(summary = "Buscar reporte por ID", description = "Obtiene el detalle de un reporte específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte encontrado"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reporte> buscarPorId(@Parameter(description = "ID del reporte", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(reporteService.buscarPorId(id));
    }

    @Operation(summary = "Crear reporte", description = "Crea un nuevo reporte de mascota perdida/hallada. Valida que la ubicación sea obligatoria y que la mascota exista.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos — falta ubicación"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @PostMapping
    public ResponseEntity<Reporte> crear(@RequestBody Reporte reporte) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reporteService.crear(reporte));
    }

    @Operation(summary = "Cambiar estado de reporte", description = "Modifica el estado de un reporte (ACTIVE → FOUND → CLOSED). Si pasa a FOUND o CLOSED, actualiza el flag en msvc-mascotas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<Reporte> cambiarEstado(@Parameter(description = "ID del reporte", example = "1") @PathVariable Long id,
                                                 @Parameter(description = "Nuevo estado (ACTIVE, FOUND, CLOSED)") @RequestParam ReporteEstado estado) {
        return ResponseEntity.ok(reporteService.cambiarEstado(id, estado));
    }

    @Operation(summary = "Buscar reportes por mascota", description = "Obtiene todos los reportes asociados a una mascota")
    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<Reporte>> buscarPorMascota(@Parameter(description = "ID de la mascota", example = "1") @PathVariable Long mascotaId) {
        return ResponseEntity.ok(reporteService.buscarPorMascota(mascotaId));
    }

    @Operation(summary = "Eliminar reporte", description = "Elimina permanentemente un reporte del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reporte eliminado"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del reporte", example = "1") @PathVariable Long id) {
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
