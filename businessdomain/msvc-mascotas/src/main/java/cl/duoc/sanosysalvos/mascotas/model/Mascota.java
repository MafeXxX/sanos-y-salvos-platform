package cl.duoc.sanosysalvos.mascotas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mascotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String especie;
    private String raza;
    private String color;
    private int edad;
    private Long usuarioId;
    private boolean tieneReporteActivo;
}
