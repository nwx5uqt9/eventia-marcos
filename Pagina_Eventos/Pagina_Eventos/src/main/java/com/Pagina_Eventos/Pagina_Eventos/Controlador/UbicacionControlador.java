package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Ubicacion;
import com.Pagina_Eventos.Pagina_Eventos.servicio.UbicacionServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ubicaciones")
@CrossOrigin(origins = "http://localhost:4200")
public class UbicacionControlador {

    private final UbicacionServicio ubicacionServicio;

    public UbicacionControlador(UbicacionServicio ubicacionServicio) {
        this.ubicacionServicio = ubicacionServicio;
    }

    @GetMapping
    public ResponseEntity<List<Ubicacion>> getAll() {
        return ResponseEntity.ok(ubicacionServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ubicacion> getById(@PathVariable Integer id) {
        return ubicacionServicio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ubicacion> create(@RequestBody Ubicacion ubicacion) {
        Ubicacion saved = ubicacionServicio.save(ubicacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ubicacion> update(@PathVariable Integer id, @RequestBody Ubicacion ubicacion) {
        return ubicacionServicio.findById(id)
                .map(existing -> {
                    existing.setNombre(ubicacion.getNombre());
                    existing.setDescripcion(ubicacion.getDescripcion());
                    existing.setCapacidad(ubicacion.getCapacidad());
                    return ResponseEntity.ok(ubicacionServicio.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (ubicacionServicio.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ubicacionServicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

