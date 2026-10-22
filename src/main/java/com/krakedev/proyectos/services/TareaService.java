package com.krakedev.proyectos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.repositories.EmpleadoRepository;
import com.krakedev.proyectos.repositories.ProyectoRepository;
import com.krakedev.proyectos.repositories.TareaRepository;

@Service
public class TareaService {

    private final TareaRepository repository;
    private final ProyectoRepository proyectoRepository;
    private final EmpleadoRepository empleadoRepository;

    public TareaService(
            TareaRepository repository,
            ProyectoRepository proyectoRepository,
            EmpleadoRepository empleadoRepository) {

        this.repository = repository;
        this.proyectoRepository = proyectoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    public Tarea guardar(Tarea tarea) {

        if (tarea.getProyecto() == null
                || tarea.getProyecto().getId() == null) {
            throw new RuntimeException("Debe indicar un proyecto");
        }

        Proyecto proyecto = proyectoRepository
                .findById(tarea.getProyecto().getId())
                .orElseThrow(() -> new RuntimeException("Proyecto no existe"));

        if (tarea.getEmpleados() == null
                || tarea.getEmpleados().isEmpty()) {
            throw new RuntimeException("Debe indicar al menos un empleado");
        }

        List<Empleado> empleadosDB = new ArrayList<>();

        for (Empleado empleado : tarea.getEmpleados()) {

            if (empleado.getId() == null) {
                throw new RuntimeException("Debe indicar el id del empleado");
            }

            Empleado empleadoReal = empleadoRepository
                    .findById(empleado.getId())
                    .orElseThrow(() -> new RuntimeException("Empleado no existe"));

            empleadosDB.add(empleadoReal);
        }

        tarea.setProyecto(proyecto);
        tarea.setEmpleados(empleadosDB);

        return repository.save(tarea);
    }

    public List<Tarea> listar() {
        return repository.findAll();
    }

    public Tarea buscar(Integer id) {

        Optional<Tarea> resultado = repository.findById(id);

        return resultado.orElse(null);
    }

    public Tarea actualizar(Integer id, Tarea datos) {

        Tarea tarea = buscar(id);

        if (tarea == null) {
            return null;
        }

        tarea.setDescripcion(datos.getDescripcion());
        tarea.setFechaLimite(datos.getFechaLimite());
        tarea.setCostoEstimado(datos.getCostoEstimado());

        return repository.save(tarea);
    }

    public boolean eliminar(Integer id) {

        Tarea tarea = buscar(id);

        if (tarea == null) {
            return false;
        }

        repository.deleteById(id);

        return true;
    }
}