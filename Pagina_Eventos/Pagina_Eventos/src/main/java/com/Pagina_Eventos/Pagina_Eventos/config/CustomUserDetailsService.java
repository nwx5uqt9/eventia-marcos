package com.Pagina_Eventos.Pagina_Eventos.config;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Usuario;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.UsuarioRepositorio;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio personalizado para cargar usuarios desde la base de datos
 * Implementa UserDetailsService de Spring Security
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;

    public CustomUserDetailsService(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar usuario por nombre de usuario
        Usuario usuario = usuarioRepositorio.findByNombreusuario(username)
                .or(() -> usuarioRepositorio.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Obtener el rol del usuario y convertirlo a autoridad de Spring Security
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (usuario.getRolUsuario() != null) {
            // Spring Security requiere el prefijo "ROLE_" para roles
            String roleName = usuario.getRolUsuario().getRol().toUpperCase();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
        }

        // Retornar UserDetails con username, password y authorities
        return User.builder()
                .username(usuario.getNombreusuario())
                .password(usuario.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}

