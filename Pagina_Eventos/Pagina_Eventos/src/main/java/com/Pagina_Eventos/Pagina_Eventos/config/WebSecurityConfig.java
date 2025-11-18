package com.Pagina_Eventos.Pagina_Eventos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Habilitar CORS con la configuración personalizada
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Deshabilitar CSRF para APIs REST (necesario para peticiones desde Angular)
                .csrf(csrf -> csrf.disable())

                // Configuración de autorización de requests
                .authorizeHttpRequests(auth -> auth
                        // ⬇ Rutas públicas (NO piden autenticación)
                        .requestMatchers(
                                "/auth/**",                   // Login y autenticación
                                "/usuarios/create",           // Registro de usuarios
                                "/usuarios/**",               // Operaciones de usuarios
                                "/eventos/**",                // Eventos públicos
                                "/boletos/**",                // Boletos
                                "/tipo-evento/**",            // Tipos de evento
                                "/estado-evento/**",          // Estados de evento
                                "/ubicacion/**",              // Ubicaciones
                                "/organizador/**",            // Organizadores
                                "/rol-usuario/**",            // Roles
                                "/boleta-venta/**",           // Boletas
                                "/lista-boleto/**",           // Lista de boletos
                                "/lista-boletos-pagados/**",  // Boletos pagados
                                "/lista-evento/**"            // Lista de eventos
                        ).permitAll()

                        // Todas las demás rutas requieren autenticación (deshabilitado por ahora)
                        .anyRequest().permitAll()
                )

                // Deshabilitar formLogin ya que es una API REST
                .formLogin(form -> form.disable())

                // Deshabilitar httpBasic ya que no lo usamos
                .httpBasic(basic -> basic.disable())

                // Configurar política de sesiones como STATELESS (sin sesiones, apropiado para REST APIs)
                .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    /**
     * Configuración de CORS para permitir peticiones desde Angular
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permitir peticiones desde el frontend Angular
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        // Permitir todos los métodos HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Permitir todos los headers
        configuration.setAllowedHeaders(List.of("*"));

        // Permitir credenciales (cookies, headers de autorización)
        configuration.setAllowCredentials(true);

        // Aplicar configuración a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * Bean para encriptar contraseñas usando BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
