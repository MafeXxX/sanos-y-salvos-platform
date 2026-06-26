package cl.duoc.sanosysalvos.bff.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bffOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BFF — Sanos y Salvos")
                        .description("Backend for Frontend que orquesta los microservicios de mascotas, reportes y usuarios. " +
                                "Utiliza patrón Facade con Circuit Breaker y Retry (Resilience4j).")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Sanos y Salvos")
                                .email("test@sanosysalvos.cl")));
    }
}
