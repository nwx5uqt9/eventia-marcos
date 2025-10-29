package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.ListaBoletosPagados;
import java.util.List;
import java.util.Optional;

public interface ListaBoletosPagadosServicio {
    List<ListaBoletosPagados> findAll();
    Optional<ListaBoletosPagados> findById(Integer id);
    ListaBoletosPagados save(ListaBoletosPagados listaBoletosPagados);
    void deleteById(Integer id);
}

