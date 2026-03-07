/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Mantenimiento.Test;

/**
 *
 * @author eiler
 */
//package com.e.gomez.Practica1AyD2.seguridad;

import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRepositorio;
import com.e.gomez.Practica1AyD2.seguridad.JwtAuthFilter;
import com.e.gomez.Practica1AyD2.seguridad.JwtService;
import com.e.gomez.Practica1AyD2.seguridad.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock private JwtService jwtService;
    @Mock private UsuarioRepositorio usuarioRepositorio;
    @Mock private TokenBlacklistService blacklist;
    @Mock private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldNotFilter_DeberiaRetornarTrueParaRutasPublicas() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/v1/auth/login");
        
        assertTrue(jwtAuthFilter.shouldNotFilter(request));
    }

    @Test
    void doFilterInternal_DeberiaPasarSiNoHayHeaderAuthorization() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_DeberiaRetornar401SiTokenEstaEnBlacklist() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-malo");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.getJti("token-malo")).thenReturn("jti-123");
        when(blacklist.isBlacklisted("jti-123")).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertTrue(response.getContentAsString().contains("Token invalidado"));
    }

    @Test
    void doFilterInternal_DeberiaAutenticarUsuarioValido() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-bueno");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Mocks de JWT
        when(jwtService.getJti("token-bueno")).thenReturn("jti-ok");
        when(blacklist.isBlacklisted("jti-ok")).thenReturn(false);
        when(jwtService.getUserId("token-bueno")).thenReturn(1);
        
        Claims claims = mock(Claims.class);
        when(claims.get("rol", String.class)).thenReturn("ADMIN");
        when(jwtService.parseClaims("token-bueno")).thenReturn(claims);

        // Mock de DB
        EntidadUsuario usuario = new EntidadUsuario();
        usuario.setId(1);
        when(usuarioRepositorio.findById(1)).thenReturn(Optional.of(usuario));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(1, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_DeberiaManejarJwtException() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-roto");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtService.getJti("token-roto")).thenThrow(new JwtException("Error"));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertTrue(response.getContentAsString().contains("Token inválido"));
    }
}
