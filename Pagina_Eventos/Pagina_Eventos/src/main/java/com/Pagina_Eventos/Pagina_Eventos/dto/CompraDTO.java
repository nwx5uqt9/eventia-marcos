package com.Pagina_Eventos.Pagina_Eventos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraDTO {
    private Integer idUsuario;
    private Integer idEvento;
    private Integer cantidad;
    private String metodoPago;
    private BigDecimal total;
}

