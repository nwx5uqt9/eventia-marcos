package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Eventos;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.EventosRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EventosServicioImpl implements EventosServicio {

    private final EventosRepositorio eventosRepositorio;

    public EventosServicioImpl(EventosRepositorio eventosRepositorio) {
        this.eventosRepositorio = eventosRepositorio;
    }

    @Override
    public List<Eventos> findAll() {
        return eventosRepositorio.findAll();
    }

    @Override
    public Optional<Eventos> findById(Integer id) {
        return eventosRepositorio.findById(id);
    }

    @Override
    public Eventos save(Eventos eventos) {
        return eventosRepositorio.save(eventos);
    }

    @Override
    public void deleteById(Integer id) {
        eventosRepositorio.deleteById(id);
    }
}

