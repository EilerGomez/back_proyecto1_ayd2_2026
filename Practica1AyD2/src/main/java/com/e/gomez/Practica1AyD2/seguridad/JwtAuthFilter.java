/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.seguridad;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRepositorio;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepositorio usuarioRepositorio;
    private final TokenBlacklistService blacklist;

    public JwtAuthFilter(JwtService jwtService, UsuarioRepositorio usuarioRepositorio, TokenBlacklistService blacklist) {
        this.jwtService = jwtService;
        this.usuarioRepositorio = usuarioRepositorio;
        this.blacklist = blacklist;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/v1/auth/login"); // login sin token
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = auth.substring(7);

        try {
            String jti = jwtService.getJti(token);
            if (blacklist.isBlacklisted(jti)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token invalidado (logout)");
                return;
            }

            Integer userId = jwtService.getUserId(token);

            EntidadUsuario user = usuarioRepositorio.findById(userId).orElse(null);
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Usuario no válido");
                return;
            }

            // rol viene en claim "rol"
           String rol = jwtService.parseClaims(token).get("rol", String.class);


            var authToken = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + rol))
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);

            chain.doFilter(request, response);

        } catch (JwtException | IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido");
        }
    }
}
