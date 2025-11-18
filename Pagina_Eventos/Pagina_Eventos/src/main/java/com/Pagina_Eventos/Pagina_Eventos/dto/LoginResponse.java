package com.Pagina_Eventos.Pagina_Eventos.dto;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta de login exitoso
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private UsuarioDTO usuario;

    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * DTO simplificado de usuario (sin contraseña)
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioDTO {
        private Integer id;
        private String nombre;
        private String apellidos;
        private String nombreusuario;
        private String email;
        private String dni;
        private Integer edad;
        private String telefono;
        private String sexo;
        private String direccion;
        private RolDTO rolUsuario;

        public static UsuarioDTO fromEntity(Usuario usuario) {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setId(usuario.getId());
            dto.setNombre(usuario.getNombre());
            dto.setApellidos(usuario.getApellidos());
            dto.setNombreusuario(usuario.getNombreusuario());
            dto.setEmail(usuario.getEmail());
            dto.setDni(usuario.getDni());
            dto.setEdad(usuario.getEdad());
            dto.setTelefono(usuario.getTelefono());
            dto.setSexo(usuario.getSexo());
            dto.setDireccion(usuario.getDireccion());

            if (usuario.getRolUsuario() != null) {
                dto.setRolUsuario(new RolDTO(
                    usuario.getRolUsuario().getId(),
                    usuario.getRolUsuario().getRol(),
                    usuario.getRolUsuario().getDescripcion()
                ));
            }

            return dto;
        }
    }

    /**
     * DTO de rol
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RolDTO {
        private Integer id;
        private String rol;
        private String descripcion;
    }
}

