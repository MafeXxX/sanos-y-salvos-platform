package cl.duoc.sanosysalvos.mascotas.service;

import cl.duoc.sanosysalvos.mascotas.model.Mascota;
import cl.duoc.sanosysalvos.mascotas.repository.MascotaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb_mascotas;DB_CLOSE_DELAY=-1",
        "spring.cloud.bootstrap.enabled=false"
})
class MascotaE2ETest {
    @Autowired private MockMvc mockMvc;
    @Autowired private MascotaRepository repo;
    @Autowired private ObjectMapper mapper;

    @BeforeEach
    void setup() { repo.deleteAll(); }

    @Test
    void flujo_eliminarMascota_retorna404AlBuscar() throws Exception {
        Mascota m = Mascota.builder().nombre("Rex").especie("Perro").usuarioId(1L).build();
        MvcResult res = mockMvc.perform(post("/api/mascotas").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(m))).andReturn();
        Long id = mapper.readTree(res.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(delete("/api/mascotas/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/mascotas/{id}", id)).andExpect(status().isNotFound());
    }
}