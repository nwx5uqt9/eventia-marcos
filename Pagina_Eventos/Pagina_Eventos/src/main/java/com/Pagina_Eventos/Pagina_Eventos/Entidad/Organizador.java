package com.Pagina_Eventos.Pagina_Eventos.Entidad;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "organizador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organizador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_empresa", nullable = false, length = 255)
    private String nombreEmpresa;

    @Column(length = 100)
    private String contacto;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}

