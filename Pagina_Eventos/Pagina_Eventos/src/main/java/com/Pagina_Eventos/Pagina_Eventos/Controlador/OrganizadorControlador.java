package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Organizador;
import com.Pagina_Eventos.Pagina_Eventos.servicio.OrganizadorServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizadores")
@CrossOrigin(origins = "http://localhost:4200")
public class OrganizadorControlador {

    private final OrganizadorServicio organizadorServicio;

    public OrganizadorControlador(OrganizadorServicio organizadorServicio) {
        this.organizadorServicio = organizadorServicio;
    }

    @GetMapping
    public ResponseEntity<List<Organizador>> getAll() {
        return ResponseEntity.ok(organizadorServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organizador> getById(@PathVariable Integer id) {
        return organizadorServicio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Organizador> create(@RequestBody Organizador organizador) {
        Organizador saved = organizadorServicio.save(organizador);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organizador> update(@PathVariable Integer id, @RequestBody Organizador organizador) {
        return organizadorServicio.findById(id)
                .map(existing -> {
                    existing.setNombreEmpresa(organizador.getNombreEmpresa());
                    existing.setDescripcion(organizador.getDescripcion());
                    existing.setTelefono(organizador.getTelefono());
                    return ResponseEntity.ok(organizadorServicio.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (organizadorServicio.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        organizadorServicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

