package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.TipoEvento;
import java.util.List;
import java.util.Optional;

public interface TipoEventoServicio {
    List<TipoEvento> findAll();
    Optional<TipoEvento> findById(Integer id);
    TipoEvento save(TipoEvento tipoEvento);
    void deleteById(Integer id);
}

