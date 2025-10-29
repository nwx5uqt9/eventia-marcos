package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Ubicacion;
import java.util.List;
import java.util.Optional;

public interface UbicacionServicio {
    List<Ubicacion> findAll();
    Optional<Ubicacion> findById(Integer id);
    Ubicacion save(Ubicacion ubicacion);
    void deleteById(Integer id);
}

