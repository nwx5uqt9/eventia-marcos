package com.Pagina_Eventos.Pagina_Eventos.servicio;

import com.Pagina_Eventos.Pagina_Eventos.Entidad.RolUsuario;
import com.Pagina_Eventos.Pagina_Eventos.Repositorio.RolUsuarioRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RolUsuarioServicioImpl implements RolUsuarioServicio {

    private final RolUsuarioRepositorio rolUsuarioRepositorio;

    public RolUsuarioServicioImpl(RolUsuarioRepositorio rolUsuarioRepositorio) {
        this.rolUsuarioRepositorio = rolUsuarioRepositorio;
    }

    @Override
    public List<RolUsuario> findAll() {
        return rolUsuarioRepositorio.findAll();
    }

    @Override
    public Optional<RolUsuario> findById(Integer id) {
        return rolUsuarioRepositorio.findById(id);
    }

    @Override
    public RolUsuario save(RolUsuario rolUsuario) {
        return rolUsuarioRepositorio.save(rolUsuario);
    }

    @Override
    public void deleteById(Integer id) {
        rolUsuarioRepositorio.deleteById(id);
    }
}

