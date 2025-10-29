package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.ListaBoleto;
import java.util.List;
import java.util.Optional;

public interface ListaBoletoServicio {
    List<ListaBoleto> findAll();
    Optional<ListaBoleto> findById(Integer id);
    ListaBoleto save(ListaBoleto listaBoleto);
    void deleteById(Integer id);
}