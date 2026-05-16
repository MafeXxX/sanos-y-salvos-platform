package cl.duoc.sanosysalvos.reportes.repository;

import cl.duoc.sanosysalvos.reportes.model.Reporte;
import cl.duoc.sanosysalvos.reportes.model.ReporteEstado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByEstado(ReporteEstado estado);
    List<Reporte> findByMascotaId(Long mascotaId);
}
