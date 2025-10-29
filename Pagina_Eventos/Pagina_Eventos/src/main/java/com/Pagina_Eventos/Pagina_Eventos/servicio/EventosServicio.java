package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Eventos;
import java.util.List;
import java.util.Optional;

public interface EventosServicio {
    List<Eventos> findAll();
    Optional<Eventos> findById(Integer id);
    Eventos save(Eventos eventos);
    void deleteById(Integer id);
}

