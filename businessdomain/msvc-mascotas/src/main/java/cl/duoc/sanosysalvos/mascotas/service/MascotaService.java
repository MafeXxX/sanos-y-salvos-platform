package cl.duoc.sanosysalvos.mascotas.service;

import cl.duoc.sanosysalvos.mascotas.builder.MascotaBuilder;
import cl.duoc.sanosysalvos.mascotas.model.Mascota;
import cl.duoc.sanosysalvos.mascotas.repository.MascotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MascotaService {

    private final MascotaRepository mascotaRepository;

    public List<Mascota> listarTodas() {
        return mascotaRepository.findAll();
    }

    public Mascota buscarPorId(Long id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con id: " + id));
    }

    public Mascota registrar(Mascota datos) {
        Mascota mascota = new MascotaBuilder()
                .nombre(datos.getNombre())
                .especie(datos.getEspecie())
                .raza(datos.getRaza())
                .color(datos.getColor())
                .edad(datos.getEdad())
                .usuarioId(datos.getUsuarioId())
                .build();
        return mascotaRepository.save(mascota);
    }

    public Mascota actualizar(Long id, Mascota datos) {
        Mascota existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setEspecie(datos.getEspecie());
        existente.setRaza(datos.getRaza());
        existente.setColor(datos.getColor());
        existente.setEdad(datos.getEdad());
        return mascotaRepository.save(existente);
    }

    public List<Mascota> buscarPorUsuario(Long usuarioId) {
        return mascotaRepository.findByUsuarioId(usuarioId);
    }

    public void actualizarReporteActivo(Long id, boolean tieneReporte) {
        Mascota mascota = buscarPorId(id);
        mascota.setTieneReporteActivo(tieneReporte);
        mascotaRepository.save(mascota);
    }
    
    public void eliminar(Long id) {
    if (!mascotaRepository.existsById(id)) {
        throw new RuntimeException("Mascota no encontrada con ID: " + id);
    }
    mascotaRepository.deleteById(id);
}
}
