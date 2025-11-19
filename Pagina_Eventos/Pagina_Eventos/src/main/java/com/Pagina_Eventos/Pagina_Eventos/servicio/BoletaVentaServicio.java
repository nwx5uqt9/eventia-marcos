package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.BoletaVenta;
import com.Pagina_Eventos.Pagina_Eventos.dto.CompraDTO;

import java.util.List;
import java.util.Optional;

public interface BoletaVentaServicio {
    BoletaVenta registrarCompra(CompraDTO compraDTO);
    List<BoletaVenta> findAll();
    Optional<BoletaVenta> findById(Integer id);
    BoletaVenta save(BoletaVenta boletaVenta);
    void deleteById(Integer id);
    List<BoletaVenta> findByUsuarioId(Integer idUsuario);
}

