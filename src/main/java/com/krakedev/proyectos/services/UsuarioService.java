package com.krakedev.proyectos.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario guardar(Usuario usuario) {

        String passwordHash = BCrypt.hashpw(
                usuario.getPassword(),
                BCrypt.gensalt());

        usuario.setPassword(passwordHash);

        return usuarioRepository.save(usuario);
    }

    public boolean autenticar(String username, String password) {

        return usuarioRepository.findByUsername(username)
                .map(usuario ->
                    BCrypt.checkpw(password, usuario.getPassword()))
                .orElse(false);
    }

    public Usuario buscarPorUsername(String username) {

        return usuarioRepository.findByUsername(username)
                .orElse(null);
    }
}