package cl.duoc.sanosysalvos.reportes;

import cl.duoc.sanosysalvos.reportes.facade.MascotaServiceFacade;
import cl.duoc.sanosysalvos.reportes.model.Reporte;
import cl.duoc.sanosysalvos.reportes.model.ReporteEstado;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:reportesdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop"
    }
)
class ReporteE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    // Simulamos la fachada que consulta a msvc-mascotas
    @MockBean
    private MascotaServiceFacade mascotaServiceFacade;

    @Test
    void flujoCompletoCrearReporte() {
        // 1. Simular que la mascota con ID 1 sí existe para que pase la validación
        MascotaServiceFacade.MascotaDTO mockMascota = new MascotaServiceFacade.MascotaDTO();
        mockMascota.setId(1L);
        mockMascota.setNombre("Max");
        Mockito.when(mascotaServiceFacade.getMascota(1L)).thenReturn(mockMascota);

        // 2. Preparar el nuevo reporte con los datos necesarios
        Reporte nuevoReporte = new Reporte();
        nuevoReporte.setMascotaId(1L);
        nuevoReporte.setUbicacion("Parque Forestal");
        nuevoReporte.setFechaRegistro(LocalDate.now());
        nuevoReporte.setEstado(ReporteEstado.valueOf("ACTIVE")); // Asumiendo que ACTIVE es parte de tu enum
        nuevoReporte.setDescripcion("Labrador perdido con collar azul");
        nuevoReporte.setTipoReporte("PERDIDA");

        // 3. Ejecutar la creación (POST)
        ResponseEntity<Reporte> responsePost = restTemplate.postForEntity("/api/reportes", nuevoReporte, Reporte.class);

        // 4. Validar que la respuesta es 201 Created y tiene un ID asignado
        assertEquals(HttpStatus.CREATED, responsePost.getStatusCode());
        assertNotNull(responsePost.getBody().getId());

        Long reporteId = responsePost.getBody().getId();

        // 5. Ejecutar la búsqueda (GET)
        ResponseEntity<Reporte> responseGet = restTemplate.getForEntity("/api/reportes/" + reporteId, Reporte.class);
        
        // 6. Validar que los datos se guardaron correctamente en la base de datos
        assertEquals(HttpStatus.OK, responseGet.getStatusCode());
        assertEquals("Parque Forestal", responseGet.getBody().getUbicacion());
    }
}