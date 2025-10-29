package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Ubicacion;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.UbicacionRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UbicacionServicioImpl implements UbicacionServicio {

    private final UbicacionRepositorio ubicacionRepositorio;

    public UbicacionServicioImpl(UbicacionRepositorio ubicacionRepositorio) {
        this.ubicacionRepositorio = ubicacionRepositorio;
    }

    @Override
    public List<Ubicacion> findAll() {
        return ubicacionRepositorio.findAll();
    }

    @Override
    public Optional<Ubicacion> findById(Integer id) {
        return ubicacionRepositorio.findById(id);
    }

    @Override
    public Ubicacion save(Ubicacion ubicacion) {
        return ubicacionRepositorio.save(ubicacion);
    }

    @Override
    public void deleteById(Integer id) {
        ubicacionRepositorio.deleteById(id);
    }
}

