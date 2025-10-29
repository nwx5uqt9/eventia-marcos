package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.RolUsuario;
import java.util.List;
import java.util.Optional;

public interface RolUsuarioServicio {
    List<RolUsuario> findAll();
    Optional<RolUsuario> findById(Integer id);
    RolUsuario save(RolUsuario rolUsuario);
    void deleteById(Integer id);
}

