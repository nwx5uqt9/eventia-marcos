package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.BoletaVenta;
import com.Pagina_Eventos.Pagina_Eventos.Entidad.Eventos;
import com.Pagina_Eventos.Pagina_Eventos.Entidad.Usuario;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.BoletaVentaRepositorio;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.EventosRepositorio;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.UsuarioRepositorio;
import com.Pagina_Eventos.Pagina_Eventos.dto.CompraDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoletaVentaServicioImpl implements BoletaVentaServicio {

    @Autowired
    private BoletaVentaRepositorio boletaVentaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private EventosRepositorio eventosRepositorio;

    @Override
    @Transactional
    public BoletaVenta registrarCompra(CompraDTO compraDTO) {
        // Validar que el usuario existe
        Usuario usuario = usuarioRepositorio.findById(compraDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar que el evento existe
        Eventos evento = eventosRepositorio.findById(compraDTO.getIdEvento())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Crear la boleta de venta
        BoletaVenta boletaVenta = new BoletaVenta();
        boletaVenta.setUsuario(usuario);
        boletaVenta.setEvento(evento);
        boletaVenta.setCantidad(compraDTO.getCantidad());
        boletaVenta.setMetodoPago(compraDTO.getMetodoPago());
        boletaVenta.setTotal(compraDTO.getTotal());
        boletaVenta.setFechaVenta(LocalDateTime.now());

        // Generar código de pago único
        String codigoPago = "BOL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        boletaVenta.setCodigoPago(codigoPago);

        // Guardar en la base de datos
        return boletaVentaRepositorio.save(boletaVenta);
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
    @Transactional
    public BoletaVenta save(BoletaVenta boletaVenta) {
        return boletaVentaRepositorio.save(boletaVenta);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        boletaVentaRepositorio.deleteById(id);
    }

    @Override
    public List<BoletaVenta> findByUsuarioId(Integer idUsuario) {
        return boletaVentaRepositorio.findByUsuarioId(idUsuario);
    }
}

