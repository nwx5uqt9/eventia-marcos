package com.Pagina_Eventos.Pagina_Eventos.Controlador;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Usuario;
import com.Pagina_Eventos.Pagina_Eventos.servicio.UsuarioServicio;
import com.Pagina_Eventos.Pagina_Eventos.servicio.ClientesPdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UsuarioControlador {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

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
    public ResponseEntity<?> create(@RequestBody Usuario usuario) {
        try {
            logger.info("=== INICIO CREACIÓN DE USUARIO ===");
            logger.info("Usuario recibido: {}", usuario.getNombreusuario());
            logger.info("Email: {}", usuario.getEmail());
            logger.info("DNI: {}", usuario.getDni());
            logger.info("Rol Usuario ID: {}", usuario.getRolUsuario() != null ? usuario.getRolUsuario().getId() : "NULL");

            // Validar que el rol no sea null
            if (usuario.getRolUsuario() == null || usuario.getRolUsuario().getId() == null) {
                logger.error("El rol de usuario es NULL o no tiene ID");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("El rol de usuario es requerido"));
            }

            logger.info("Iniciando guardado en base de datos...");
            Usuario saved = usuarioServicio.save(usuario);
            logger.info("Usuario creado exitosamente con ID: {}", saved.getId());
            logger.info("=== FIN CREACIÓN DE USUARIO ===");

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            logger.error("Error de validación al crear usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Error inesperado al crear usuario", e);
            logger.error("Tipo de excepción: {}", e.getClass().getName());
            logger.error("Mensaje: {}", e.getMessage());
            if (e.getCause() != null) {
                logger.error("Causa raíz: {}", e.getCause().getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error al crear usuario: " + e.getMessage()));
        }
    }

    // Clase interna para respuestas de error
    private static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return usuarioServicio.findById(id)
                .map(existing -> {
                    existing.setNombre(usuario.getNombre());
                    existing.setApellidos(usuario.getApellidos());
                    existing.setNombreusuario(usuario.getNombreusuario());
                    existing.setEmail(usuario.getEmail());

                    // Solo actualizar la contraseña si se proporciona una nueva (no vacía)
                    if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                        existing.setPassword(usuario.getPassword());
                    }

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
            logger.error("Error al generar reporte PDF de clientes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

