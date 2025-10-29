package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.EstadoEvento;
import com.Pagina_Eventos.Pagina_Eventos.servicio.EstadoEventoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estado")
@CrossOrigin(origins = "http://localhost:4200")
public class EstadoEventoControlador {

    private final EstadoEventoServicio estadoEventoServicio;

    public EstadoEventoControlador(EstadoEventoServicio estadoEventoServicio) {
        this.estadoEventoServicio = estadoEventoServicio;
    }

    @GetMapping
    public ResponseEntity<List<EstadoEvento>> getAll() {
        return ResponseEntity.ok(estadoEventoServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoEvento> getById(@PathVariable Integer id) {
        return estadoEventoServicio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<EstadoEvento> create(@RequestBody EstadoEvento estadoEvento) {
        EstadoEvento saved = estadoEventoServicio.save(estadoEvento);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoEvento> update(@PathVariable Integer id, @RequestBody EstadoEvento estadoEvento) {
        return estadoEventoServicio.findById(id)
                .map(existing -> {
                    existing.setNombre(estadoEvento.getNombre());
                    existing.setDescripcion(estadoEvento.getDescripcion());
                    return ResponseEntity.ok(estadoEventoServicio.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (estadoEventoServicio.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        estadoEventoServicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

