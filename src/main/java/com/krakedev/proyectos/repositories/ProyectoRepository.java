package com.krakedev.proyectos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krakedev.proyectos.entidades.Proyecto;

public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {

}