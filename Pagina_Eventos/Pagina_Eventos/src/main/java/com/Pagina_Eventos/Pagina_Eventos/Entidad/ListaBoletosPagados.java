package com.Pagina_Eventos.Pagina_Eventos.Entidad;

import jakarta.persistence.*;

@Entity
@Table(name = "lista_boletos_pagados")
public class ListaBoletosPagados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_boleta_venta", nullable = false)
    private BoletaVenta boletaVenta;

    @ManyToOne
    @JoinColumn(name = "id_boleto", nullable = false)
    private Boleto boleto;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Integer cantidad = 1;

    public ListaBoletosPagados() {
    }

    public ListaBoletosPagados(Integer id, BoletaVenta boletaVenta, Boleto boleto,
                               Usuario usuario, Integer cantidad) {
        this.id = id;
        this.boletaVenta = boletaVenta;
        this.boleto = boleto;
        this.usuario = usuario;
        this.cantidad = cantidad != null ? cantidad : 1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BoletaVenta getBoletaVenta() {
        return boletaVenta;
    }

    public void setBoletaVenta(BoletaVenta boletaVenta) {
        this.boletaVenta = boletaVenta;
    }

    public Boleto getBoleto() {
        return boleto;
    }

    public void setBoleto(Boleto boleto) {
        this.boleto = boleto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}


