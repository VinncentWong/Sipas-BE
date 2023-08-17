package bcc.sipas.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(
                        new Info()
                                .description("Sipas backend implementation")
                                .title("SIPAS")
                )
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("Bearer")
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Application source code")
                                .url("https://github.com/VinncentWong/Sipas-BE")
                )
                .components(
                        new Components()
                );
    }
}
