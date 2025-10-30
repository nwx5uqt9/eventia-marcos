package com.Pagina_Eventos.Pagina_Eventos.Entidad;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lista_evento", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_ubicacion", "id_evento"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListaEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ubicacion", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Ubicacion ubicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Eventos evento;
}