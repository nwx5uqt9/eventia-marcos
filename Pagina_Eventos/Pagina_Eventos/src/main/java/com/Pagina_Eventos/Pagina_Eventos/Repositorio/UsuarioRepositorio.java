package com.Pagina_Eventos.Pagina_Eventos.Repositorio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

    /**
     * Verifica si existe un usuario con el DNI especificado
     */
    boolean existsByDni(String dni);

    /**
     * Verifica si existe un usuario con el email especificado
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un usuario con el nombre de usuario especificado
     */
    boolean existsByNombreusuario(String nombreusuario);

    /**
     * Busca un usuario por DNI
     */
    Optional<Usuario> findByDni(String dni);

    /**
     * Busca un usuario por email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca un usuario por nombre de usuario
     */
    Optional<Usuario> findByNombreusuario(String nombreusuario);
}



