package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.RolUsuario;
import com.Pagina_Eventos.Pagina_Eventos.servicio.RolUsuarioServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@CrossOrigin(origins = "http://localhost:4200")
public class RolUsuarioControlador {

    private final RolUsuarioServicio rolUsuarioServicio;

    public RolUsuarioControlador(RolUsuarioServicio rolUsuarioServicio) {
        this.rolUsuarioServicio = rolUsuarioServicio;
    }

    @GetMapping
    public ResponseEntity<List<RolUsuario>> getAll() {
        return ResponseEntity.ok(rolUsuarioServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolUsuario> getById(@PathVariable Integer id) {
        return rolUsuarioServicio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<RolUsuario> create(@RequestBody RolUsuario rolUsuario) {
        RolUsuario saved = rolUsuarioServicio.save(rolUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolUsuario> update(@PathVariable Integer id, @RequestBody RolUsuario rolUsuario) {
        return rolUsuarioServicio.findById(id)
                .map(existing -> {
                    existing.setRol(rolUsuario.getRol());
                    existing.setDescripcion(rolUsuario.getDescripcion());
                    return ResponseEntity.ok(rolUsuarioServicio.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (rolUsuarioServicio.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        rolUsuarioServicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

