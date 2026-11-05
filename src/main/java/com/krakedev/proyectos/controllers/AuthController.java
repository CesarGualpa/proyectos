package com.krakedev.proyectos.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.security.JwtUtil;
import com.krakedev.proyectos.services.TokenBlacklistService;
import com.krakedev.proyectos.services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService blacklistService;

    public AuthController(
            UsuarioService usuarioService,
            JwtUtil jwtUtil,
            TokenBlacklistService blacklistService) {

        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(
            @RequestBody Usuario usuario) {

        try {

            Usuario nuevo =
                    usuarioService.guardar(usuario);

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

            String username =
                    credenciales.get("username");

            String password =
                    credenciales.get("password");

            boolean autenticado =
                    usuarioService.autenticar(
                            username,
                            password);

            if (!autenticado) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(
                            "Usuario o contraseña incorrecta");
            }

            Usuario usuario =
                    usuarioService.buscarPorUsername(username);

            String token = jwtUtil.generarToken(
                    usuario.getUsername(),
                    usuario.getRol());

            return ResponseEntity.ok(
                    Map.of("token", token));

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error durante el login: "
                            + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authHeader) {

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Token no proporcionado");
        }

        String token = authHeader.substring(7);

        blacklistService.invalidarToken(token);

        return ResponseEntity.ok(
                Map.of(
                    "mensaje",
                    "Sesion cerrada exitosamente. "
                    + "Token invalidado"));
    }

    /*
     * Este endpoint será reemplazado en el Hito 3
     * para utilizar Spring Security.
     */
    @GetMapping("/perfil")
    public ResponseEntity<?> perfilTemporal() {

        return ResponseEntity.ok(
                Map.of(
                    "mensaje",
                    "Spring Security se integrara "
                    + "en el siguiente hito"));
    }
}