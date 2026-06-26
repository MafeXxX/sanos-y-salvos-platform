package cl.duoc.sanosysalvos.usuarios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI usuariosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("msvc-usuarios — Sanos y Salvos")
                        .description("Microservicio de gestión de usuarios. " +
                                "Utiliza patrón Adapter (OpenFeign) para exponer las mascotas asociadas a cada usuario.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Sanos y Salvos")
                                .email("test@sanosysalvos.cl")));
    }
}
