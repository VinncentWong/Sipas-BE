package bcc.sipas;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableR2dbcRepositories
@OpenAPIDefinition(
		info = @Info(
				title = "Sipas backend implementation",
				description = "Backend for sipas application"
		),
		externalDocs = @ExternalDocumentation(
				description = "Github url",
				url = "https://github.com/VinncentWong/Sipas-BE"
		),
		security = {
				@SecurityRequirement(
						name = "basicAuth"
				)
		}
)
@SecurityScheme(
		type = SecuritySchemeType.HTTP,
		name = "basicAuth",
		scheme = "basic"
)
@SecurityScheme(
		description = "Bearer token",
		name = "bearerAuth",
		bearerFormat = "JWT",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		in = SecuritySchemeIn.HEADER
)
public class SipasApplication {

	public static void main(String[] args) {
		SpringApplication.run(SipasApplication.class, args);
	}

}
