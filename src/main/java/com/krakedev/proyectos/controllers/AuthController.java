package com.krakedev.proyectos.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {

        try {

            Usuario nuevo = usuarioService.guardar(usuario);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "id", nuevo.getId(),
                            "username", nuevo.getUsername(),
                            "rol", nuevo.getRol()));

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar usuario: "
                            + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> credenciales) {

        try {

            String username = credenciales.get("username");
            String password = credenciales.get("password");

            boolean autenticado =
                    usuarioService.autenticar(username, password);

            if (!autenticado) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Usuario o contraseña incorrecta");
            }

            Usuario usuario =
                    usuarioService.buscarPorUsername(username);

            return ResponseEntity.ok(
                    Map.of(
                            "mensaje", "Login exitoso",
                            "username", usuario.getUsername(),
                            "rol", usuario.getRol()));

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error durante el login: "
                            + e.getMessage());
        }
    }
}