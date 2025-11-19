package com.Pagina_Eventos.Pagina_Eventos.Entidad;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "boleta_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoletaVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Eventos evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ubicacion")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Ubicacion ubicacion;

    @Column(name = "codigo_pago", unique = true, nullable = false, length = 100)
    private String codigoPago;

    @Column(name = "fecha_venta")
    private LocalDateTime fechaVenta;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @PrePersist
    protected void onCreate() {
        if (fechaVenta == null) {
            fechaVenta = LocalDateTime.now();
        }
    }
}
