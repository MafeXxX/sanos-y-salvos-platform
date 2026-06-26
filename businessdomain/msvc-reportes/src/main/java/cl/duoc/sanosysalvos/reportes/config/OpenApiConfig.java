package cl.duoc.sanosysalvos.reportes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reportesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("msvc-reportes — Sanos y Salvos")
                        .description("Microservicio de gestión de reportes de mascotas perdidas/halladas. " +
                                "Utiliza patrón Facade (OpenFeign) para comunicarse con msvc-mascotas.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Sanos y Salvos")
                                .email("test@sanosysalvos.cl")));
    }
}
