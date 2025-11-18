package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.dto.LoginRequest;
import com.Pagina_Eventos.Pagina_Eventos.dto.LoginResponse;
import com.Pagina_Eventos.Pagina_Eventos.servicio.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticación
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthControlador {

    private static final Logger logger = LoggerFactory.getLogger(AuthControlador.class);
    
    private final AuthService authService;

    public AuthControlador(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint de login
     * POST /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
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
        
        // Autenticar usuario
        LoginResponse response = authService.authenticate(loginRequest);
        
        // Devolver respuesta apropiada
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
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
}

