package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Boleto;
import com.Pagina_Eventos.Pagina_Eventos.servicio.BoletoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boletos")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class BoletoControlador {

    private final BoletoServicio boletoServicio;

    public BoletoControlador(BoletoServicio boletoServicio) {
        this.boletoServicio = boletoServicio;
    }

    @GetMapping
    public ResponseEntity<List<Boleto>> getAll() {
        return ResponseEntity.ok(boletoServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Boleto> getById(@PathVariable Integer id) {
        return boletoServicio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Boleto> create(@RequestBody Boleto boleto) {
        Boleto saved = boletoServicio.save(boleto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boleto> update(@PathVariable Integer id, @RequestBody Boleto boleto) {
        return boletoServicio.findById(id)
                .map(existing -> {
                    existing.setNombre(boleto.getNombre());
                    existing.setPrecio(boleto.getPrecio());
                    existing.setEvento(boleto.getEvento());
                    return ResponseEntity.ok(boletoServicio.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (boletoServicio.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        boletoServicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

