package cl.duoc.sanosysalvos.mascotas.controller;

import cl.duoc.sanosysalvos.mascotas.model.Mascota;
import cl.duoc.sanosysalvos.mascotas.service.MascotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor
@Tag(name = "Mascotas", description = "CRUD de mascotas — registra nombre, especie, raza, color, edad y dueño")
public class MascotaController {

    private final MascotaService mascotaService;

    @Operation(summary = "Listar todas las mascotas", description = "Obtiene la lista completa de mascotas registradas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Mascota>> listar() {
        return ResponseEntity.ok(mascotaService.listarTodas());
    }

    @Operation(summary = "Buscar mascota por ID", description = "Obtiene el detalle de una mascota específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mascota encontrada"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> buscarPorId(@Parameter(description = "ID de la mascota", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(mascotaService.buscarPorId(id));
    }

    @Operation(summary = "Registrar mascota", description = "Crea una nueva mascota. Valida nombre, especie y usuarioId mediante el patrón Builder")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mascota creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos — falta nombre, especie o usuarioId")
    })
    @PostMapping
    public ResponseEntity<Mascota> registrar(@Valid @RequestBody Mascota mascota) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mascotaService.registrar(mascota));
    }

    @Operation(summary = "Actualizar mascota", description = "Modifica los datos de una mascota existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mascota actualizada"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Mascota> actualizar(@Parameter(description = "ID de la mascota", example = "1") @PathVariable Long id,
                                              @Valid @RequestBody Mascota mascota) {
        return ResponseEntity.ok(mascotaService.actualizar(id, mascota));
    }

    @Operation(summary = "Eliminar mascota", description = "Elimina permanentemente una mascota del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mascota eliminada"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID de la mascota", example = "1") @PathVariable Long id) {
        mascotaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar mascotas por usuario", description = "Obtiene todas las mascotas asociadas a un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Mascota>> buscarPorUsuario(@Parameter(description = "ID del usuario", example = "1") @PathVariable Long usuarioId) {
        return ResponseEntity.ok(mascotaService.buscarPorUsuario(usuarioId));
    }

    @Operation(summary = "Actualizar estado de reporte", description = "Marca si una mascota tiene un reporte activo (perdida/hallada)")
    @PutMapping("/{id}/reporte-activo")
    public ResponseEntity<Void> actualizarReporteActivo(@Parameter(description = "ID de la mascota", example = "1") @PathVariable Long id,
                                                        @Parameter(description = "true si tiene reporte activo") @RequestParam boolean activo) {
        mascotaService.actualizarReporteActivo(id, activo);
        return ResponseEntity.ok().build();
    }
}
