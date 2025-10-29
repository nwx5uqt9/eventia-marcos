package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.ListaEvento;
import com.Pagina_Eventos.Pagina_Eventos.servicio.ListaEventoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lista-eventos")
@CrossOrigin(origins = "http://localhost:4200")
public class ListaEventoControlador {

    private final ListaEventoServicio listaEventoServicio;

    public ListaEventoControlador(ListaEventoServicio listaEventoServicio) {
        this.listaEventoServicio = listaEventoServicio;
    }

    @GetMapping
    public ResponseEntity<List<ListaEvento>> getAll() {
        return ResponseEntity.ok(listaEventoServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaEvento> getById(@PathVariable Integer id) {
        return listaEventoServicio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ListaEvento> create(@RequestBody ListaEvento listaEvento) {
        ListaEvento saved = listaEventoServicio.save(listaEvento);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaEvento> update(@PathVariable Integer id, @RequestBody ListaEvento listaEvento) {
        return listaEventoServicio.findById(id)
                .map(existing -> {
                    existing.setUbicacion(listaEvento.getUbicacion());
                    existing.setEvento(listaEvento.getEvento());
                    return ResponseEntity.ok(listaEventoServicio.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (listaEventoServicio.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        listaEventoServicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

