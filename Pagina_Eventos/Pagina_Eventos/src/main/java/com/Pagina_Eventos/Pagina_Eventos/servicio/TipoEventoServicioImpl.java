package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.TipoEvento;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.TipoEventoRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TipoEventoServicioImpl implements TipoEventoServicio {

    private final TipoEventoRepositorio tipoEventoRepositorio;

    public TipoEventoServicioImpl(TipoEventoRepositorio tipoEventoRepositorio) {
        this.tipoEventoRepositorio = tipoEventoRepositorio;
    }

    @Override
    public List<TipoEvento> findAll() {
        return tipoEventoRepositorio.findAll();
    }

    @Override
    public Optional<TipoEvento> findById(Integer id) {
        return tipoEventoRepositorio.findById(id);
    }

    @Override
    public TipoEvento save(TipoEvento tipoEvento) {
        return tipoEventoRepositorio.save(tipoEvento);
    }

    @Override
    public void deleteById(Integer id) {
        tipoEventoRepositorio.deleteById(id);
    }
}

