package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Usuario;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.UsuarioRepositorio;
import com.Pagina_Eventos.Pagina_Eventos.dto.LoginRequest;
import com.Pagina_Eventos.Pagina_Eventos.dto.LoginResponse;
import com.Pagina_Eventos.Pagina_Eventos.servicio.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticación
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthControlador {

    private static final Logger logger = LoggerFactory.getLogger(AuthControlador.class);
    
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepositorio usuarioRepositorio;

    public AuthControlador(AuthService authService,
                          AuthenticationManager authenticationManager,
                          UsuarioRepositorio usuarioRepositorio) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    /**
     * Endpoint de login con autenticación de Spring Security
     * POST /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        logger.info("Intento de login para usuario: {}", loginRequest.getUsername());
        
        // Validar que los campos no estén vacíos
        if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse(false, "El nombre de usuario es requerido"));
        }
        
        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse(false, "La contraseña es requerida"));
        }
        
        try {
            // Autenticar con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // Establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Crear o obtener la sesión HTTP
            HttpSession session = request.getSession(true);
            logger.info("Sesión creada con ID: {}", session.getId());

            // Buscar el usuario para devolver sus datos completos
            Usuario usuario = usuarioRepositorio.findByNombreusuario(loginRequest.getUsername())
                    .or(() -> usuarioRepositorio.findByEmail(loginRequest.getUsername()))
                    .orElse(null);

            if (usuario != null) {
                LoginResponse.UsuarioDTO usuarioDTO = LoginResponse.UsuarioDTO.fromEntity(usuario);
                return ResponseEntity.ok(new LoginResponse(true, "Login exitoso", usuarioDTO));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new LoginResponse(false, "Error al cargar datos del usuario"));
            }

        } catch (Exception e) {
            logger.error("Error en autenticación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, "Usuario o contraseña incorrectos"));
        }
    }

    /**
     * Endpoint para verificar si un usuario existe
     * GET /auth/check/{username}
     */
    @GetMapping("/check/{username}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable String username) {
        boolean exists = authService.userExists(username);
        return ResponseEntity.ok(exists);
    }

    /**
     * Endpoint para cerrar sesión
     * POST /auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            logger.info("Sesión cerrada");
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().body(java.util.Map.of("message", "Logout exitoso"));
    }
}

