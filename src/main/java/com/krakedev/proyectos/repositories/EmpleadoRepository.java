package com.krakedev.proyectos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krakedev.proyectos.entidades.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

}