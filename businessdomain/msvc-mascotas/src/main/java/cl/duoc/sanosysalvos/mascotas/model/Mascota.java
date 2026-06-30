package cl.duoc.sanosysalvos.mascotas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "mascotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa una mascota registrada en la plataforma")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la mascota", example = "1")
    private Long id;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Schema(description = "Nombre de la mascota", example = "Firulais")
    private String nombre;

    @NotBlank(message = "La especie de la mascota es obligatoria")
    @Schema(description = "Especie de la mascota", example = "Perro")
    private String especie;

    @Schema(description = "Raza de la mascota", example = "Labrador")
    private String raza;

    @Schema(description = "Color de la mascota", example = "Negro")
    private String color;

    @Schema(description = "Edad de la mascota en años", example = "3")
    private int edad;

    @NotNull(message = "El usuarioId es obligatorio")
    @Schema(description = "ID del usuario propietario", example = "1")
    private Long usuarioId;

    @Schema(description = "Indica si la mascota tiene un reporte activo (perdida/hallada)")
    private boolean tieneReporteActivo;
}
