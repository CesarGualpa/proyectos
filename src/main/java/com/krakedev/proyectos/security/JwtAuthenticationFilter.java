package com.krakedev.proyectos.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.krakedev.proyectos.services.TokenBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final TokenBlacklistService blacklistService;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(
            TokenBlacklistService blacklistService,
            JwtUtil jwtUtil) {

        this.blacklistService = blacklistService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (blacklistService.estaInvalidado(token)) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType(
                    "application/json");

            response.getWriter().write(
                    "{\"error\":\"Sesion cerrada. "
                    + "Token invalidado\"}");

            return;
        }

        DecodedJWT datosToken =
                jwtUtil.validarToken(token);

        if (datosToken == null) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType(
                    "application/json");

            response.getWriter().write(
                    "{\"error\":\"Token invalido "
                    + "o expirado\"}");

            return;
        }

        String username =
                datosToken.getSubject();

        String rol =
                datosToken.getClaim("rol")
                        .asString();

        String rolSpring =
                "ROLE_" + rol;

        SimpleGrantedAuthority autoridad =
                new SimpleGrantedAuthority(
                        rolSpring);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singleton(
                                autoridad));

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}