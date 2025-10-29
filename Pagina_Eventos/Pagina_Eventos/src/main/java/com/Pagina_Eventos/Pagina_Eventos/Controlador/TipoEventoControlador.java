package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.TipoEvento;
import com.Pagina_Eventos.Pagina_Eventos.servicio.TipoEventoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipo")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoEventoControlador {

    private final TipoEventoServicio tipoEventoServicio;

    public TipoEventoControlador(TipoEventoServicio tipoEventoServicio) {
        this.tipoEventoServicio = tipoEventoServicio;
    }

    @GetMapping
    public ResponseEntity<List<TipoEvento>> getAll() {
        return ResponseEntity.ok(tipoEventoServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoEvento> getById(@PathVariable Integer id) {
        return tipoEventoServicio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<TipoEvento> create(@RequestBody TipoEvento tipoEvento) {
        TipoEvento saved = tipoEventoServicio.save(tipoEvento);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoEvento> update(@PathVariable Integer id, @RequestBody TipoEvento tipoEvento) {
        return tipoEventoServicio.findById(id)
                .map(existing -> {
                    existing.setNombre(tipoEvento.getNombre());
                    existing.setDescripcion(tipoEvento.getDescripcion());
                    return ResponseEntity.ok(tipoEventoServicio.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (tipoEventoServicio.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        tipoEventoServicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

