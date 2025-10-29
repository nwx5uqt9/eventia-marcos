package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.ListaEvento;
import java.util.List;
import java.util.Optional;

public interface ListaEventoServicio {
    List<ListaEvento> findAll();
    Optional<ListaEvento> findById(Integer id);
    ListaEvento save(ListaEvento listaEvento);
    void deleteById(Integer id);
}

