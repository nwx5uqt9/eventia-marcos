package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.Boleto;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.BoletoRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BoletoServicioImpl implements BoletoServicio {

    private final BoletoRepositorio boletoRepositorio;

    public BoletoServicioImpl(BoletoRepositorio boletoRepositorio) {
        this.boletoRepositorio = boletoRepositorio;
    }

    @Override
    public List<Boleto> findAll() {
        return boletoRepositorio.findAll();
    }

    @Override
    public Optional<Boleto> findById(Integer id) {
        return boletoRepositorio.findById(id);
    }

    @Override
    public Boleto save(Boleto boleto) {
        return boletoRepositorio.save(boleto);
    }

    @Override
    public void deleteById(Integer id) {
        boletoRepositorio.deleteById(id);
    }
}

