package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Organizador;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.OrganizadorRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizadorServicioImpl implements OrganizadorServicio {

    private final OrganizadorRepositorio organizadorRepositorio;

    public OrganizadorServicioImpl(OrganizadorRepositorio organizadorRepositorio) {
        this.organizadorRepositorio = organizadorRepositorio;
    }

    @Override
    public List<Organizador> findAll() {
        return organizadorRepositorio.findAll();
    }

    @Override
    public Optional<Organizador> findById(Integer id) {
        return organizadorRepositorio.findById(id);
    }

    @Override
    public Organizador save(Organizador organizador) {
        return organizadorRepositorio.save(organizador);
    }

    @Override
    public void deleteById(Integer id) {
        organizadorRepositorio.deleteById(id);
    }
}

