package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Usuario;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.UsuarioRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServicioImpl.class);

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServicioImpl(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepositorio.findById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        try {
            logger.info("=== UsuarioServicio.save() INICIO ===");
            logger.info("Usuario ID: {}", usuario.getId());
            logger.info("Nombreusuario: {}", usuario.getNombreusuario());
            logger.info("Email: {}", usuario.getEmail());
            logger.info("DNI: {}", usuario.getDni());

            // Validar duplicados solo para nuevos usuarios o si se cambian los campos únicos
            if (usuario.getId() == null) {
                logger.info("Es un nuevo usuario, validando campos únicos...");
                // Nuevo usuario - validar todos los campos únicos
                validateUniqueFields(usuario);
                logger.info("Validación de campos únicos completada");
            } else {
                logger.info("Es una actualización de usuario existente");
                // Usuario existente - validar solo si se modificaron los campos únicos
                Optional<Usuario> existingUser = usuarioRepositorio.findById(usuario.getId());
                if (existingUser.isPresent()) {
                    Usuario existing = existingUser.get();

                    // Validar DNI si cambió y no está vacío
                    if (usuario.getDni() != null && !usuario.getDni().trim().isEmpty()
                        && !usuario.getDni().equals(existing.getDni())) {
                        if (usuarioRepositorio.existsByDni(usuario.getDni())) {
                            throw new IllegalArgumentException("El DNI ya está registrado");
                        }
                    }

                    // Validar email si cambió
                    if (usuario.getEmail() != null && !usuario.getEmail().equals(existing.getEmail())) {
                        if (usuario.getEmail().trim().isEmpty()) {
                            throw new IllegalArgumentException("El email no puede estar vacío");
                        }
                        if (usuarioRepositorio.existsByEmail(usuario.getEmail())) {
                            throw new IllegalArgumentException("El email ya está registrado");
                        }
                    }

                    // Validar nombre de usuario si cambió
                    if (usuario.getNombreusuario() != null && !usuario.getNombreusuario().equals(existing.getNombreusuario())) {
                        if (usuario.getNombreusuario().trim().isEmpty()) {
                            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
                        }
                        if (usuarioRepositorio.existsByNombreusuario(usuario.getNombreusuario())) {
                            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
                        }
                    }
                }
            }

            // Encriptar la contraseña antes de guardar solo si es nueva o se modificó
            logger.info("Verificando contraseña...");
            if (usuario.getId() == null || isPasswordModified(usuario)) {
                logger.info("Encriptando contraseña...");
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }

            logger.info("Guardando usuario en base de datos...");
            Usuario saved = usuarioRepositorio.save(usuario);
            logger.info("Usuario guardado exitosamente con ID: {}", saved.getId());
            logger.info("=== UsuarioServicio.save() FIN ===");

            return saved;
        } catch (Exception e) {
            logger.error("ERROR en UsuarioServicio.save(): {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteById(Integer id) {
        usuarioRepositorio.deleteById(id);
    }

    /**
     * Valida que los campos únicos no existan en la base de datos
     */
    private void validateUniqueFields(Usuario usuario) {
        // Solo validar DNI si no está vacío
        if (usuario.getDni() != null && !usuario.getDni().trim().isEmpty()) {
            if (usuarioRepositorio.existsByDni(usuario.getDni())) {
                throw new IllegalArgumentException("El DNI ya está registrado");
            }
        }

        // Validar email (siempre requerido)
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (usuarioRepositorio.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Validar nombre de usuario (siempre requerido)
        if (usuario.getNombreusuario() == null || usuario.getNombreusuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (usuarioRepositorio.existsByNombreusuario(usuario.getNombreusuario())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
    }

    /**
     * Verifica si la contraseña fue modificada comparándola con la almacenada
     */
    private boolean isPasswordModified(Usuario usuario) {
        if (usuario.getId() == null) {
            return true; // Es un nuevo usuario
        }

        Optional<Usuario> existingUser = usuarioRepositorio.findById(usuario.getId());
        if (existingUser.isPresent()) {
            // Si la contraseña no está encriptada (no empieza con $2a$), significa que fue modificada
            String currentPassword = usuario.getPassword();
            String storedPassword = existingUser.get().getPassword();
            return !currentPassword.equals(storedPassword);
        }

        return true;
    }
}

