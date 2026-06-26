package cl.duoc.sanosysalvos.reportes.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa un reporte de mascota perdida/hallada")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del reporte", example = "1")
    private Long id;

    @Schema(description = "ID de la mascota asociada al reporte", example = "1")
    private Long mascotaId;

    @Schema(description = "Ubicación donde se perdió/encontró la mascota", example = "Santiago Centro")
    private String ubicacion;

    @Schema(description = "Fecha en que se registró el reporte", example = "2025-01-15")
    private LocalDate fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado actual del reporte", example = "ACTIVE")
    private ReporteEstado estado;

    @Schema(description = "Descripción adicional del reporte", example = "Labrador negro con collar rojo")
    private String descripcion;

    @Schema(description = "Tipo de reporte", example = "PERDIDA")
    private String tipoReporte;
}
