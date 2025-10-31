package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Usuario;
import com.Pagina_Eventos.Pagina_Eventos.servicio.UsuarioServicio;
import com.Pagina_Eventos.Pagina_Eventos.servicio.ClientesPdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;
    private final ClientesPdfService clientesPdfService;

    public UsuarioControlador(UsuarioServicio usuarioServicio, ClientesPdfService clientesPdfService) {
        this.usuarioServicio = usuarioServicio;
        this.clientesPdfService = clientesPdfService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(usuarioServicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Integer id) {
        return usuarioServicio.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario) {
        Usuario saved = usuarioServicio.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return usuarioServicio.findById(id)
                .map(existing -> {
                    existing.setNombre(usuario.getNombre());
                    existing.setApellidos(usuario.getApellidos());
                    existing.setNombreusuario(usuario.getNombreusuario());
                    existing.setEmail(usuario.getEmail());
                    existing.setPassword(usuario.getPassword());
                    existing.setDni(usuario.getDni());
                    existing.setEdad(usuario.getEdad());
                    existing.setTelefono(usuario.getTelefono());
                    existing.setSexo(usuario.getSexo());
                    existing.setDireccion(usuario.getDireccion());
                    existing.setRolUsuario(usuario.getRolUsuario());
                    return ResponseEntity.ok(usuarioServicio.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (usuarioServicio.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        usuarioServicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reporte/pdf")
    public ResponseEntity<byte[]> generarReportePdf() {
        try {
            byte[] pdfBytes = clientesPdfService.generarReporteClientes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_clientes.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

