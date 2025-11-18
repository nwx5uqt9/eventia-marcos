package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Usuario;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.UsuarioRepositorio;
import com.Pagina_Eventos.Pagina_Eventos.dto.LoginRequest;
import com.Pagina_Eventos.Pagina_Eventos.dto.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio de autenticación para login de usuarios
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Autenticar usuario con nombre de usuario/email y contraseña
     * @param loginRequest Datos de login (username y password)
     * @return LoginResponse con el resultado de la autenticación
     */
    public LoginResponse authenticate(LoginRequest loginRequest) {
        try {
            // Buscar usuario por nombre de usuario o email
            Optional<Usuario> usuarioOpt = findByUsernameOrEmail(loginRequest.getUsername());

            if (usuarioOpt.isEmpty()) {
                logger.warn("Intento de login fallido: usuario '{}' no encontrado", loginRequest.getUsername());
                return new LoginResponse(false, "Usuario o contraseña incorrectos");
            }

            Usuario usuario = usuarioOpt.get();

            // Verificar la contraseña usando BCrypt
            boolean passwordMatch = passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword());

            if (!passwordMatch) {
                logger.warn("Intento de login fallido: contraseña incorrecta para usuario '{}'", loginRequest.getUsername());
                return new LoginResponse(false, "Usuario o contraseña incorrectos");
            }

            // Login exitoso
            logger.info("Login exitoso para usuario: {}", usuario.getNombreusuario());

            // Crear DTO sin la contraseña
            LoginResponse.UsuarioDTO usuarioDTO = LoginResponse.UsuarioDTO.fromEntity(usuario);

            return new LoginResponse(true, "Login exitoso", usuarioDTO);

        } catch (Exception e) {
            logger.error("Error durante la autenticación", e);
            return new LoginResponse(false, "Error al procesar la autenticación");
        }
    }

    /**
     * Busca un usuario por nombre de usuario o email
     */
    private Optional<Usuario> findByUsernameOrEmail(String username) {
        // Primero intentar buscar por nombre de usuario
        Optional<Usuario> usuario = usuarioRepositorio.findByNombreusuario(username);

        // Si no se encuentra, intentar por email
        if (usuario.isEmpty()) {
            usuario = usuarioRepositorio.findByEmail(username);
        }

        return usuario;
    }

    /**
     * Verifica si un usuario existe por nombre de usuario o email
     */
    public boolean userExists(String username) {
        return findByUsernameOrEmail(username).isPresent();
    }
}

