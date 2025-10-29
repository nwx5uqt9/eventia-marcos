package com.Pagina_Eventos.Pagina_Eventos.Entidad;

import jakarta.persistence.*;

@Entity
@Table(name = "lista_boleto", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_ubicacion", "id_boleto"})
})
public class ListaBoleto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ubicacion", nullable = false)
    private Ubicacion ubicacion;

    @ManyToOne
    @JoinColumn(name = "id_boleto", nullable = false)
    private Boleto boleto;

    public ListaBoleto() {
    }

    public ListaBoleto(Integer id, Ubicacion ubicacion, Boleto boleto) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.boleto = boleto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Boleto getBoleto() {
        return boleto;
    }

    public void setBoleto(Boleto boleto) {
        this.boleto = boleto;
    }
}

