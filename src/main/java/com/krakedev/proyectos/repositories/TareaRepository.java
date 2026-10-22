package com.krakedev.proyectos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krakedev.proyectos.entidades.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Integer> {

}