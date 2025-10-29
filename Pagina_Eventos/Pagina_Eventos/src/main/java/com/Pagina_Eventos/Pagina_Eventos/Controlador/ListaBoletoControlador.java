package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.ListaBoleto;
import com.Pagina_Eventos.Pagina_Eventos.servicio.ListaBoletoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lista-boletos")
@CrossOrigin(origins = "http://localhost:4200")
public class ListaBoletoControlador {

    private final ListaBoletoServicio listaBoletoServicio;

    public ListaBoletoControlador(ListaBoletoServicio listaBoletoServicio) {
        this.listaBoletoServicio = listaBoletoServicio;
    }

    @GetMapping
    public ResponseEntity<List<ListaBoleto>> getAll() {
        return ResponseEntity.ok(listaBoletoServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaBoleto> getById(@PathVariable Integer id) {
        return listaBoletoServicio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ListaBoleto> create(@RequestBody ListaBoleto listaBoleto) {
        ListaBoleto saved = listaBoletoServicio.save(listaBoleto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaBoleto> update(@PathVariable Integer id, @RequestBody ListaBoleto listaBoleto) {
        return listaBoletoServicio.findById(id)
                .map(existing -> {
                    existing.setUbicacion(listaBoleto.getUbicacion());
                    existing.setBoleto(listaBoleto.getBoleto());
                    return ResponseEntity.ok(listaBoletoServicio.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (listaBoletoServicio.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        listaBoletoServicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}