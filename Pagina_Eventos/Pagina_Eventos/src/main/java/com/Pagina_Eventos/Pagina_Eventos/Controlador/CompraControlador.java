package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.BoletaVenta;
import com.Pagina_Eventos.Pagina_Eventos.dto.CompraDTO;
import com.Pagina_Eventos.Pagina_Eventos.servicio.BoletaVentaServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class CompraControlador {

    private static final Logger logger = LoggerFactory.getLogger(CompraControlador.class);

    @Autowired
    private BoletaVentaServicio boletaVentaServicio;

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrarCompra(@RequestBody CompraDTO compraDTO) {
        Map<String, Object> response = new HashMap<>();

        logger.info("Recibida solicitud de compra - Usuario: {}, Evento: {}, Cantidad: {}, Método: {}",
                    compraDTO.getIdUsuario(),
                    compraDTO.getIdEvento(),
                    compraDTO.getCantidad(),
                    compraDTO.getMetodoPago());

        // Validaciones básicas
        if (compraDTO.getIdUsuario() == null || compraDTO.getIdEvento() == null) {
            response.put("success", false);
            response.put("message", "Usuario y evento son obligatorios");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (compraDTO.getCantidad() == null || compraDTO.getCantidad() < 1 || compraDTO.getCantidad() > 10) {
            response.put("success", false);
            response.put("message", "La cantidad debe estar entre 1 y 10 boletos");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (compraDTO.getMetodoPago() == null || compraDTO.getMetodoPago().trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "El método de pago es obligatorio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (compraDTO.getTotal() == null || compraDTO.getTotal().doubleValue() <= 0) {
            response.put("success", false);
            response.put("message", "El total debe ser mayor a 0");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            BoletaVenta boletaVenta = boletaVentaServicio.registrarCompra(compraDTO);

            logger.info("Compra registrada exitosamente - Código: {}, Boleta ID: {}",
                        boletaVenta.getCodigoPago(),
                        boletaVenta.getId());

            response.put("success", true);
            response.put("message", "Compra registrada exitosamente");
            response.put("codigoPago", boletaVenta.getCodigoPago());
            response.put("boletaId", boletaVenta.getId());
            response.put("total", boletaVenta.getTotal());
            response.put("cantidad", boletaVenta.getCantidad());
            response.put("metodoPago", boletaVenta.getMetodoPago());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error al registrar la compra: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error al registrar la compra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error("Error interno del servidor: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}



