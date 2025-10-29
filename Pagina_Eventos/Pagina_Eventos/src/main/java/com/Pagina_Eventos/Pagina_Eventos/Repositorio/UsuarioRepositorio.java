package com.Pagina_Eventos.Pagina_Eventos.Repositorio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
}

