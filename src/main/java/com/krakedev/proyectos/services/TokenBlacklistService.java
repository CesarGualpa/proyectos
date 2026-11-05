package com.krakedev.proyectos.services;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

    private final Set<String> blacklist =
            ConcurrentHashMap.newKeySet();

    public void invalidarToken(String token) {

        blacklist.add(token);
    }

    public boolean estaInvalidado(String token) {

        return blacklist.contains(token);
    }
}