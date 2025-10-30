package com.Pagina_Eventos.Pagina_Eventos.Entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rol_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 50)
    private String rol;

    @Column(length = 255)
    private String descripcion;
}

