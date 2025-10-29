package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.ListaEvento;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.ListaEventoRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ListaEventoServicioImpl implements ListaEventoServicio {

    private final ListaEventoRepositorio listaEventoRepositorio;

    public ListaEventoServicioImpl(ListaEventoRepositorio listaEventoRepositorio) {
        this.listaEventoRepositorio = listaEventoRepositorio;
    }

    @Override
    public List<ListaEvento> findAll() {
        return listaEventoRepositorio.findAll();
    }

    @Override
    public Optional<ListaEvento> findById(Integer id) {
        return listaEventoRepositorio.findById(id);
    }

    @Override
    public ListaEvento save(ListaEvento listaEvento) {
        return listaEventoRepositorio.save(listaEvento);
    }

    @Override
    public void deleteById(Integer id) {
        listaEventoRepositorio.deleteById(id);
    }
}

