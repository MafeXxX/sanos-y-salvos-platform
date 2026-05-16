package cl.duoc.sanosysalvos.mascotas.repository;

import cl.duoc.sanosysalvos.mascotas.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByUsuarioId(Long usuarioId);
}
