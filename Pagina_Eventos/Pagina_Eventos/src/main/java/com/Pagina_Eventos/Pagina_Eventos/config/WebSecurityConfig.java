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
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Configurar CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Deshabilitar CSRF para APIs REST
                .csrf(csrf -> csrf.disable())

                // Configurar autorización de requests
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas
                        .requestMatchers(
                                "/auth/**",
                                "/usuarios/create",
                                "/eventos",
                                "/eventos/{id}"
                        ).permitAll()

                        // Rutas que requieren rol ADMIN
                        .requestMatchers(
                                "/usuarios/**",
                                "/eventos/**",
                                "/boletos/**",
                                "/ventas/**",
                                "/rol-usuario/**",
                                "/tipo-evento/**",
                                "/estado-evento/**",
                                "/ubicacion/**",
                                "/organizador/**"
                        ).hasRole("ADMIN")

                        // Rutas autenticadas (cualquier rol)
                        .requestMatchers(
                                "/api/compras/**"
                        ).authenticated()

                        // Cualquier otra petición debe estar autenticada
                        .anyRequest().authenticated()
                )

                // Deshabilitar formLogin
                .formLogin(form -> form.disable())

                // Deshabilitar httpBasic
                .httpBasic(basic -> basic.disable())

                // Política de sesiones STATELESS
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return httpSecurity.build();
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

        // Permitir credenciales
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
