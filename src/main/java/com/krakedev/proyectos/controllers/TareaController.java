package com.krakedev.proyectos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.services.TareaService;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService service;

    public TareaController(TareaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Tarea tarea) {

        try {

            if (tarea.getProyecto() == null
                    || tarea.getProyecto().getId() == null
                    || tarea.getEmpleados() == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Debe indicar un proyecto y los empleados");
            }

            Tarea nueva = service.guardar(tarea);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nueva);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar tarea: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {

        try {

            List<Tarea> tareas = service.listar();

            return ResponseEntity.ok(tareas);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al listar tareas: " + e.getMessage());
        }
    }
}