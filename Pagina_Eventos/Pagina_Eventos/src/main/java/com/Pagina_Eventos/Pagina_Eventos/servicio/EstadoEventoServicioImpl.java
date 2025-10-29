package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.EstadoEvento;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.EstadoEventoRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EstadoEventoServicioImpl implements EstadoEventoServicio {

    private final EstadoEventoRepositorio estadoEventoRepositorio;

    public EstadoEventoServicioImpl(EstadoEventoRepositorio estadoEventoRepositorio) {
        this.estadoEventoRepositorio = estadoEventoRepositorio;
    }

    @Override
    public List<EstadoEvento> findAll() {
        return estadoEventoRepositorio.findAll();
    }

    @Override
    public Optional<EstadoEvento> findById(Integer id) {
        return estadoEventoRepositorio.findById(id);
    }

    @Override
    public EstadoEvento save(EstadoEvento estadoEvento) {
        return estadoEventoRepositorio.save(estadoEvento);
    }

    @Override
    public void deleteById(Integer id) {
        estadoEventoRepositorio.deleteById(id);
    }
}

