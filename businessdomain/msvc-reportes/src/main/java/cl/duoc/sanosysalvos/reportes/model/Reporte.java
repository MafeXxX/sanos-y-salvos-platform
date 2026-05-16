package cl.duoc.sanosysalvos.reportes.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mascotaId;
    private String ubicacion;
    private LocalDate fechaRegistro;

    @Enumerated(EnumType.STRING)
    private ReporteEstado estado;

    private String descripcion;
    private String tipoReporte;
}
