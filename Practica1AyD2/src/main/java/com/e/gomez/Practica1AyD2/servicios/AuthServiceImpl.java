/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAuth.LoginRequest;
import com.e.gomez.Practica1AyD2.dtoAuth.LoginResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioCompletoResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionCredencialesInvalidas;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCartera;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import com.e.gomez.Practica1AyD2.modelos.EntidadRol;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.modelos.Entidad_Usuario_Rol;
import com.e.gomez.Practica1AyD2.repositorios.CarteraRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.PerfilRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.RolRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRolRepositorio;
import com.e.gomez.Practica1AyD2.seguridad.JwtService;
import com.e.gomez.Practica1AyD2.seguridad.TokenBlacklistService;
import io.jsonwebtoken.JwtException;
import java.util.Date;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioRolRepositorio usuarioRolRepositorio;
    private final RolRepositorio rolRepositorio;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwtService;
    private final TokenBlacklistService blacklist;
    private final PerfilRepositorio perfilRepositorio;
    private final CarteraRepositorio carteraRepositorio;

    public AuthServiceImpl(
            UsuarioRepositorio usuarioRepositorio,
            UsuarioRolRepositorio usuarioRolRepositorio,
            RolRepositorio rolRepositorio,
            BCryptPasswordEncoder encoder,
            JwtService jwtService,
            TokenBlacklistService blacklist,
            PerfilRepositorio perfilRepositorio,
            CarteraRepositorio carteraRepositorio
    ) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioRolRepositorio = usuarioRolRepositorio;
        this.rolRepositorio = rolRepositorio;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.blacklist = blacklist;
        this.perfilRepositorio=perfilRepositorio;
        this.carteraRepositorio=carteraRepositorio;
    }

    @Override
    public LoginResponse login(LoginRequest req) throws ExcepcionNoExiste, ExcepcionCredencialesInvalidas {
        if (req.getIdentificador() == null || req.getIdentificador().isBlank()) {
            throw new ExcepcionCredencialesInvalidas("Identificador requerido");
        }
        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new ExcepcionCredencialesInvalidas("Password requerido");
        }

        String ident = req.getIdentificador().trim();

        EntidadUsuario user = usuarioRepositorio
                .findByUsernameIgnoreCaseOrCorreoIgnoreCase(ident, ident)
                .orElseThrow(() -> new ExcepcionNoExiste("Usuario no encontrado"));

        // Ajustá esto a tu getter real
        String hashed = user.getPassword_hash();

        if (!encoder.matches(req.getPassword(), hashed)) {
            throw new ExcepcionCredencialesInvalidas("Credenciales inválidas");
        }

        Entidad_Usuario_Rol ur = usuarioRolRepositorio.findByIdUsuarioId(user.getId());
        if (ur == null) {
            throw new ExcepcionNoExiste("Usuario no tiene rol asignado");
        }


        Integer rolId = ur.getId().getRolId();

        EntidadRol rol = rolRepositorio.findById(rolId)
                .orElseThrow(() -> new ExcepcionNoExiste("Rol no encontrado"));

        String token = jwtService.generateToken(user.getId(), user.getUsername(), rol.getNombre());
        
        EntidadPerfil perfil = perfilRepositorio.findByUsuarioId(user.getId())
                 .orElseThrow(() -> new ExcepcionNoExiste("Perfil no encontrado para usuarioId: " + user.getId()));
        
        EntidadCartera cartera = carteraRepositorio.findByUsuarioId(user.getId())
                .orElseThrow(()-> new ExcepcionNoExiste("Cartera no encontrada para el usuario: "+user.getId()));
        
        return new LoginResponse(
                token,
                "Bearer",
                jwtService.getExpirationMs(),
                new UsuarioCompletoResponse(new UsuarioResponse(user), perfil, rol,cartera)
        );
    }

    @Override
    public void logout(String token) throws ExcepcionCredencialesInvalidas {
        try {
            String jti = jwtService.getJti(token);
            long expAt = jwtService.getExpirationEpochMs(token);
            Date expiresAt = new Date(expAt);
            blacklist.blacklist(jti, expiresAt);

        } catch (JwtException | IllegalArgumentException e) {
            throw new ExcepcionCredencialesInvalidas("Token inválido para logout");
        }
    }
}

