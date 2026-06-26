package cl.duoc.sanosysalvos.usuarios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa un usuario registrado en la plataforma")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del usuario", example = "1")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Column(unique = true)
    @Schema(description = "Correo electrónico único del usuario", example = "juan@example.com")
    private String email;

    @Schema(description = "Número de teléfono de contacto", example = "+56912345678")
    private String telefono;

    @Schema(description = "Dirección del usuario", example = "Av. Providencia 1234")
    private String direccion;
}
