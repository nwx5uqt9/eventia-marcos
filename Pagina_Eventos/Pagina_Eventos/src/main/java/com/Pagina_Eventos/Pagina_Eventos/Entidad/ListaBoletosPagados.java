package com.Pagina_Eventos.Pagina_Eventos.Entidad;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lista_boletos_pagados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListaBoletosPagados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_boleta_venta", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BoletaVenta boletaVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_boleto", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Boleto boleto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    @Column(nullable = false)
    private Integer cantidad;

    @PrePersist
    protected void onCreate() {
        if (cantidad == null) {
            cantidad = 1;
        }
    }
}


