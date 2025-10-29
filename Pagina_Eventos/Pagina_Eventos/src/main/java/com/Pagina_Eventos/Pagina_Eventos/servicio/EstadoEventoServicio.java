package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.EstadoEvento;
import java.util.List;
import java.util.Optional;

public interface EstadoEventoServicio {
    List<EstadoEvento> findAll();
    Optional<EstadoEvento> findById(Integer id);
    EstadoEvento save(EstadoEvento estadoEvento);
    void deleteById(Integer id);
}

