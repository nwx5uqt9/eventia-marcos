package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.BoletaVenta;
import com.Pagina_Eventos.Pagina_Eventos.servicio.BoletaVentaServicio;
import com.Pagina_Eventos.Pagina_Eventos.servicio.VentasPdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
@CrossOrigin(origins = "http://localhost:4200")
public class BoletaVentaControlador {

    private final BoletaVentaServicio boletaVentaServicio;
    private final VentasPdfService ventasPdfService;

    public BoletaVentaControlador(BoletaVentaServicio boletaVentaServicio, VentasPdfService ventasPdfService) {
        this.boletaVentaServicio = boletaVentaServicio;
        this.ventasPdfService = ventasPdfService;
    }

    @GetMapping
    public ResponseEntity<List<BoletaVenta>> getAll() {
        return ResponseEntity.ok(boletaVentaServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoletaVenta> getById(@PathVariable Integer id) {
        return boletaVentaServicio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<BoletaVenta>> getByUsuario(@PathVariable Integer idUsuario) {
        List<BoletaVenta> boletas = boletaVentaServicio.findByUsuarioId(idUsuario);
        return ResponseEntity.ok(boletas);
    }

    @PostMapping
    public ResponseEntity<BoletaVenta> create(@RequestBody BoletaVenta boletaVenta) {
        BoletaVenta saved = boletaVentaServicio.save(boletaVenta);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoletaVenta> update(@PathVariable Integer id, @RequestBody BoletaVenta boletaVenta) {
        return boletaVentaServicio.findById(id)
                .map(existing -> {
                    existing.setUsuario(boletaVenta.getUsuario());
                    existing.setEvento(boletaVenta.getEvento());
                    existing.setUbicacion(boletaVenta.getUbicacion());
                    existing.setCodigoPago(boletaVenta.getCodigoPago());
                    existing.setFechaVenta(boletaVenta.getFechaVenta());
                    existing.setTotal(boletaVenta.getTotal());
                    return ResponseEntity.ok(boletaVentaServicio.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (boletaVentaServicio.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        boletaVentaServicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reporte/pdf")
    public ResponseEntity<byte[]> generarReportePdf() {
        try {
            byte[] pdfBytes = ventasPdfService.generarReporteVentas();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_ventas.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

