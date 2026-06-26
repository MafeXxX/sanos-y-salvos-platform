package cl.duoc.sanosysalvos.mascotas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mascotasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("msvc-mascotas — Sanos y Salvos")
                        .description("Microservicio de gestión de mascotas. CRUD completo con validación mediante patrón Builder.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Sanos y Salvos")
                                .email("test@sanosysalvos.cl")));
    }
}
