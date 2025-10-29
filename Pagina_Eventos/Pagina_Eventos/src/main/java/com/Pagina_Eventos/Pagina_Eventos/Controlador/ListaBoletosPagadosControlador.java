package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.ListaBoletosPagados;
import com.Pagina_Eventos.Pagina_Eventos.servicio.ListaBoletosPagadosServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boletos-pagados")
@CrossOrigin(origins = "http://localhost:4200")
public class ListaBoletosPagadosControlador {

    private final ListaBoletosPagadosServicio listaBoletosPagadosServicio;

    public ListaBoletosPagadosControlador(ListaBoletosPagadosServicio listaBoletosPagadosServicio) {
        this.listaBoletosPagadosServicio = listaBoletosPagadosServicio;
    }

    @GetMapping
    public ResponseEntity<List<ListaBoletosPagados>> getAll() {
        return ResponseEntity.ok(listaBoletosPagadosServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListaBoletosPagados> getById(@PathVariable Integer id) {
        return listaBoletosPagadosServicio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ListaBoletosPagados> create(@RequestBody ListaBoletosPagados listaBoletosPagados) {
        ListaBoletosPagados saved = listaBoletosPagadosServicio.save(listaBoletosPagados);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListaBoletosPagados> update(@PathVariable Integer id, @RequestBody ListaBoletosPagados listaBoletosPagados) {
        return listaBoletosPagadosServicio.findById(id)
                .map(existing -> {
                    existing.setBoletaVenta(listaBoletosPagados.getBoletaVenta());
                    existing.setBoleto(listaBoletosPagados.getBoleto());
                    existing.setUsuario(listaBoletosPagados.getUsuario());
                    existing.setCantidad(listaBoletosPagados.getCantidad());
                    return ResponseEntity.ok(listaBoletosPagadosServicio.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (listaBoletosPagadosServicio.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        listaBoletosPagadosServicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


