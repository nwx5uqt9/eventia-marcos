package com.Pagina_Eventos.Pagina_Eventos.Repositorio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.ListaBoletosPagados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaBoletosPagadosRepositorio extends JpaRepository<ListaBoletosPagados, Integer> {
}


