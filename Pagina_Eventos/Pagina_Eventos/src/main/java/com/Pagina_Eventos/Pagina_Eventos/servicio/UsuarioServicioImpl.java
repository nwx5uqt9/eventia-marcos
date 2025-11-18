package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Usuario;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.UsuarioRepositorio;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

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
        // Validar duplicados solo para nuevos usuarios o si se cambian los campos únicos
        if (usuario.getId() == null) {
            // Nuevo usuario - validar todos los campos únicos
            validateUniqueFields(usuario);
        } else {
            // Usuario existente - validar solo si se modificaron los campos únicos
            Optional<Usuario> existingUser = usuarioRepositorio.findById(usuario.getId());
            if (existingUser.isPresent()) {
                Usuario existing = existingUser.get();

                // Validar DNI si cambió
                if (usuario.getDni() != null && !usuario.getDni().equals(existing.getDni())) {
                    if (usuarioRepositorio.existsByDni(usuario.getDni())) {
                        throw new IllegalArgumentException("El DNI ya está registrado");
                    }
                }

                // Validar email si cambió
                if (usuario.getEmail() != null && !usuario.getEmail().equals(existing.getEmail())) {
                    if (usuarioRepositorio.existsByEmail(usuario.getEmail())) {
                        throw new IllegalArgumentException("El email ya está registrado");
                    }
                }

                // Validar nombre de usuario si cambió
                if (usuario.getNombreusuario() != null && !usuario.getNombreusuario().equals(existing.getNombreusuario())) {
                    if (usuarioRepositorio.existsByNombreusuario(usuario.getNombreusuario())) {
                        throw new IllegalArgumentException("El nombre de usuario ya está en uso");
                    }
                }
            }
        }

        // Encriptar la contraseña antes de guardar solo si es nueva o se modificó
        if (usuario.getId() == null || isPasswordModified(usuario)) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepositorio.save(usuario);
    }

    @Override
    public void deleteById(Integer id) {
        usuarioRepositorio.deleteById(id);
    }

    /**
     * Valida que los campos únicos no existan en la base de datos
     */
    private void validateUniqueFields(Usuario usuario) {
        if (usuario.getDni() != null && usuarioRepositorio.existsByDni(usuario.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado");
        }

        if (usuario.getEmail() != null && usuarioRepositorio.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        if (usuario.getNombreusuario() != null && usuarioRepositorio.existsByNombreusuario(usuario.getNombreusuario())) {
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

