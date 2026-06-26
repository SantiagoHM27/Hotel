package com.example.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth

                        // Login público
                        .pathMatchers(HttpMethod.POST, "/api/login").permitAll()

                        // Huéspedes: ADMIN y USER pueden registrar y consultar
                        .pathMatchers(HttpMethod.GET, "/api/huespedes/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                        .pathMatchers(HttpMethod.POST, "/api/huespedes/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                        .pathMatchers(HttpMethod.PUT, "/api/huespedes/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                        .pathMatchers(HttpMethod.DELETE, "/api/huespedes/**")
                        .hasAuthority("ROLE_ADMIN")

                        // Habitaciones:
                        // USER solo consulta
                        .pathMatchers(HttpMethod.GET, "/api/habitaciones/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                        // ADMIN crea, modifica, elimina, cambia estado/precio
                        .pathMatchers(HttpMethod.POST, "/api/habitaciones/**")
                        .hasAuthority("ROLE_ADMIN")

                        .pathMatchers(HttpMethod.PUT, "/api/habitaciones/**")
                        .hasAuthority("ROLE_ADMIN")

                        .pathMatchers(HttpMethod.DELETE, "/api/habitaciones/**")
                        .hasAuthority("ROLE_ADMIN")

                        // Reservas: ADMIN y USER pueden crear, consultar, modificar, check-in/check-out
                        .pathMatchers("/api/reservas/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                        // Usuarios: solo ADMIN
                        .pathMatchers("/api/usuarios/**")
                        .hasAuthority("ROLE_ADMIN")

                        // Reportes: solo ADMIN
                        .pathMatchers("/api/reportes/**")
                        .hasAuthority("ROLE_ADMIN")

                        // Cualquier otra ruta requiere autenticación
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    private ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("");

        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter =
                new ReactiveJwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                new ReactiveJwtGrantedAuthoritiesConverterAdapter(authoritiesConverter)
        );

        return jwtAuthenticationConverter;
    }
}