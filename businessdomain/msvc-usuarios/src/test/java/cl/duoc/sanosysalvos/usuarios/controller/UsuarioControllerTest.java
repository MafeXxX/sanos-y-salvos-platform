package cl.duoc.sanosysalvos.usuarios.controller;

import cl.duoc.sanosysalvos.usuarios.model.Usuario;
import cl.duoc.sanosysalvos.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UsuarioController.class, properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false"
})
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarDeberiaRetornar200() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorIdDeberiaRetornar200() throws Exception {
        Usuario usuario = new Usuario(1L, "Juan", "juan@test.com", null, null);
        when(usuarioService.buscarPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void registrarDeberiaRetornar201() throws Exception {
        Usuario input = new Usuario(null, "Ana", "ana@test.com", null, null);
        Usuario guardado = new Usuario(1L, "Ana", "ana@test.com", null, null);
        when(usuarioService.registrar(any(Usuario.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated());
    }

    @Test
    void actualizarDeberiaRetornar200() throws Exception {
        Usuario input = new Usuario(null, "Ana", "ana@test.com", null, null);
        when(usuarioService.actualizar(anyLong(), any(Usuario.class))).thenReturn(input);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk());
    }

    @Test
    void getMascotasDeberiaRetornar200() throws Exception {
        when(usuarioService.getMascotasDeUsuario(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/usuarios/1/mascotas"))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarDeberiaRetornar204() throws Exception {
        doNothing().when(usuarioService).eliminar(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}
