package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Eventos;
import com.Pagina_Eventos.Pagina_Eventos.servicio.EventosServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventos")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class EventosControlador {

    private final EventosServicio eventosServicio;

    public EventosControlador(EventosServicio eventosServicio) {
        this.eventosServicio = eventosServicio;
    }

    @GetMapping
    public ResponseEntity<List<Eventos>> getAll() {
        return ResponseEntity.ok(eventosServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Eventos> getById(@PathVariable Integer id) {
        return eventosServicio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Eventos> create(@RequestBody Eventos eventos) {
        Eventos saved = eventosServicio.save(eventos);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Eventos> update(@PathVariable Integer id, @RequestBody Eventos eventos) {
        return eventosServicio.findById(id)
                .map(existing -> {
                    existing.setNombre(eventos.getNombre());
                    existing.setDescripcion(eventos.getDescripcion());
                    existing.setFechaHora(eventos.getFechaHora());
                    existing.setTipoEvento(eventos.getTipoEvento());
                    existing.setOrganizador(eventos.getOrganizador());
                    existing.setEstadoEvento(eventos.getEstadoEvento());
                    existing.setUbicacion(eventos.getUbicacion());
                    existing.setPrecio(eventos.getPrecio());
                    return ResponseEntity.ok(eventosServicio.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (eventosServicio.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        eventosServicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

