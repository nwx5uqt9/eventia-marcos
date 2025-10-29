package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Boleto;
import java.util.List;
import java.util.Optional;

public interface BoletoServicio {
    List<Boleto> findAll();
    Optional<Boleto> findById(Integer id);
    Boleto save(Boleto boleto);
    void deleteById(Integer id);
}

