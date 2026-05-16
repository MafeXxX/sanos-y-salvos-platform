package cl.duoc.sanosysalvos.usuarios.service;

import cl.duoc.sanosysalvos.usuarios.adapter.MascotaClientAdapter;
import cl.duoc.sanosysalvos.usuarios.model.Usuario;
import cl.duoc.sanosysalvos.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final MascotaClientAdapter mascotaClientAdapter;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario datos) {
        Usuario existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setEmail(datos.getEmail());
        existente.setTelefono(datos.getTelefono());
        existente.setDireccion(datos.getDireccion());
        return usuarioRepository.save(existente);
    }

    public List<MascotaClientAdapter.MascotaDTO> getMascotasDeUsuario(Long usuarioId) {
        buscarPorId(usuarioId);
        return mascotaClientAdapter.getMascotasByUsuario(usuarioId);
    }
}
