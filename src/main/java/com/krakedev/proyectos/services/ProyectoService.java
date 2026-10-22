package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.repositories.ProyectoRepository;

@Service
public class ProyectoService {

    private final ProyectoRepository repository;

    public ProyectoService(ProyectoRepository repository) {
        this.repository = repository;
    }

    public Proyecto guardar(Proyecto proyecto) {
        return repository.save(proyecto);
    }

    public List<Proyecto> listar() {
        return repository.findAll();
    }

    public Proyecto buscar(Integer id) {
        Optional<Proyecto> resultado = repository.findById(id);

        return resultado.orElse(null);
    }

    public Proyecto actualizar(Integer id, Proyecto datos) {

        Proyecto proyecto = buscar(id);

        if (proyecto == null) {
            return null;
        }

        proyecto.setNombre(datos.getNombre());
        proyecto.setDescripcion(datos.getDescripcion());
        proyecto.setFechaInicio(datos.getFechaInicio());

        return repository.save(proyecto);
    }

    public boolean eliminar(Integer id) {

        Proyecto proyecto = buscar(id);

        if (proyecto == null) {
            return false;
        }

        repository.deleteById(id);

        return true;
    }
}