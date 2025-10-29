package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Organizador;
import java.util.List;
import java.util.Optional;

public interface OrganizadorServicio {
    List<Organizador> findAll();
    Optional<Organizador> findById(Integer id);
    Organizador save(Organizador organizador);
    void deleteById(Integer id);
}

