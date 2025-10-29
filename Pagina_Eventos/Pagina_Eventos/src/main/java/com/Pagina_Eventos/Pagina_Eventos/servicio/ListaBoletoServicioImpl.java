package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.ListaBoleto;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.ListaBoletoRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ListaBoletoServicioImpl implements ListaBoletoServicio {

    private final ListaBoletoRepositorio listaBoletoRepositorio;

    public ListaBoletoServicioImpl(ListaBoletoRepositorio listaBoletoRepositorio) {
        this.listaBoletoRepositorio = listaBoletoRepositorio;
    }

    @Override
    public List<ListaBoleto> findAll() {
        return listaBoletoRepositorio.findAll();
    }

    @Override
    public Optional<ListaBoleto> findById(Integer id) {
        return listaBoletoRepositorio.findById(id);
    }

    @Override
    public ListaBoleto save(ListaBoleto listaBoleto) {
        return listaBoletoRepositorio.save(listaBoleto);
    }

    @Override
    public void deleteById(Integer id) {
        listaBoletoRepositorio.deleteById(id);
    }
}

