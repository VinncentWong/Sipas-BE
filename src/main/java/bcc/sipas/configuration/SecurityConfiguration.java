package bcc.sipas.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        return http
                .cors((c) -> {
                    c.configurationSource(r -> {
                        CorsConfiguration cors = new CorsConfiguration();
                        cors.setAllowedHeaders(List.of(CorsConfiguration.ALL));
                        cors.setAllowedMethods(List.of(CorsConfiguration.ALL));
                        cors.setAllowedOrigins(List.of(CorsConfiguration.ALL));
                        return cors;
                    });
                })
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .exceptionHandling((c) -> {

                })
                .build();
    }
}
