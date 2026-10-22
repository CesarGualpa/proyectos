package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.repositories.EmpleadoRepository;

@Service
public class EmpleadoService {

    private final EmpleadoRepository repository;

    public EmpleadoService(EmpleadoRepository repository) {
        this.repository = repository;
    }

    public Empleado guardar(Empleado empleado) {
        return repository.save(empleado);
    }

    public List<Empleado> listar() {
        return repository.findAll();
    }

    public Empleado buscar(Integer id) {

        Optional<Empleado> resultado = repository.findById(id);

        return resultado.orElse(null);
    }

    public Empleado actualizar(Integer id, Empleado datos) {

        Empleado empleado = buscar(id);

        if (empleado == null) {
            return null;
        }

        empleado.setNombre(datos.getNombre());
        empleado.setCargo(datos.getCargo());

        return repository.save(empleado);
    }

    public boolean eliminar(Integer id) {

        Empleado empleado = buscar(id);

        if (empleado == null) {
            return false;
        }

        repository.deleteById(id);

        return true;
    }
}