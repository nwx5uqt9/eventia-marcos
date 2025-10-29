package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.BoletaVenta;
import java.util.List;
import java.util.Optional;

public interface BoletaVentaServicio {
    List<BoletaVenta> findAll();
    Optional<BoletaVenta> findById(Integer id);
    BoletaVenta save(BoletaVenta boletaVenta);
    void deleteById(Integer id);
}

