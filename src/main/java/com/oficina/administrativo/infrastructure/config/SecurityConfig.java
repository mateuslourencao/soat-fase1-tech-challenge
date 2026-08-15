package com.oficina.administrativo.infrastructure.config;

import com.oficina.administrativo.domain.exception.SecurityConfigurationException;
import com.oficina.administrativo.infrastructure.adapters.inbound.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) {
        try {
            return http
                    // Desabilitado CSRF pois a API é STATELESS (utiliza JWT) e não utiliza Cookies/Sessão.
                    // Isso mitiga os riscos de ataques CSRF por padrão.
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .exceptionHandling(handler -> handler.authenticationEntryPoint(
                            (request, response, exception) -> response.sendError(401, "Autenticação necessária")))
                    .authorizeHttpRequests(authorization -> authorization
                            .requestMatchers(HttpMethod.GET,
                                    "/v3/api-docs",
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/error").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/administrativo/autenticacao").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/ordensdeservico/{id}").permitAll()
                            .anyRequest().authenticated())
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        } catch (Exception e) {
            throw new SecurityConfigurationException("Erro ao configurar a corrente de filtros de segurança", e);
        }
    }
}
