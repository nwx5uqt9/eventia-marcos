package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.BoletaVenta;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.BoletaVentaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoletaVentaServicioImpl implements BoletaVentaServicio {

    private final BoletaVentaRepositorio boletaVentaRepositorio;

    @Autowired
    public BoletaVentaServicioImpl(BoletaVentaRepositorio boletaVentaRepositorio) {
        this.boletaVentaRepositorio = boletaVentaRepositorio;
    }

    @Override
    public List<BoletaVenta> findAll() {
        return boletaVentaRepositorio.findAll();
    }

    @Override
    public Optional<BoletaVenta> findById(Integer id) {
        return boletaVentaRepositorio.findById(id);
    }

    @Override
    public BoletaVenta save(BoletaVenta boletaVenta) {
        return boletaVentaRepositorio.save(boletaVenta);
    }

    @Override
    public void deleteById(Integer id) {
        boletaVentaRepositorio.deleteById(id);
    }
}