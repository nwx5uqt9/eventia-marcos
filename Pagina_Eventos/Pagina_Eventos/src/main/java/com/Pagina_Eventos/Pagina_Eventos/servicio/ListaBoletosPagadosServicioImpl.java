package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.ListaBoletosPagados;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.ListaBoletosPagadosRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ListaBoletosPagadosServicioImpl implements ListaBoletosPagadosServicio {

    private final ListaBoletosPagadosRepositorio listaBoletosPagadosRepositorio;

    public ListaBoletosPagadosServicioImpl(ListaBoletosPagadosRepositorio listaBoletosPagadosRepositorio) {
        this.listaBoletosPagadosRepositorio = listaBoletosPagadosRepositorio;
    }

    @Override
    public List<ListaBoletosPagados> findAll() {
        return listaBoletosPagadosRepositorio.findAll();
    }

    @Override
    public Optional<ListaBoletosPagados> findById(Integer id) {
        return listaBoletosPagadosRepositorio.findById(id);
    }

    @Override
    public ListaBoletosPagados save(ListaBoletosPagados listaBoletosPagados) {
        return listaBoletosPagadosRepositorio.save(listaBoletosPagados);
    }

    @Override
    public void deleteById(Integer id) {
        listaBoletosPagadosRepositorio.deleteById(id);
    }
}


