/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */
//package com.e.gomez.Practica1AyD2.servicios.implementaciones;

import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.modelos.PasswordResetToken;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.TokenRepositorio;
import com.e.gomez.Practica1AyD2.servicios.RecuperacionService;
import com.e.gomez.Practica1AyD2.servicios.EmailService;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RecuperacionServiceImpl implements RecuperacionService {

    private final UsuarioRepositorio usuarioRepo;
    private final TokenRepositorio tokenRepo;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioService serviceUsuario;

    public RecuperacionServiceImpl(UsuarioRepositorio usuarioRepo, TokenRepositorio tokenRepo, 
                                   EmailService emailService, PasswordEncoder passwordEncoder, UsuarioService serviceUsuario) {
        this.usuarioRepo = usuarioRepo;
        this.tokenRepo = tokenRepo;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.serviceUsuario=serviceUsuario;
    }

    @Override
    @Transactional
    public void solicitarRecuperacion(String identificador) throws ExcepcionNoExiste {
        // Buscamos al usuario por username o correo electrónico
        EntidadUsuario usuario = usuarioRepo.findByUsernameIgnoreCaseOrCorreoIgnoreCase(identificador,identificador)
                .orElseThrow(() -> new ExcepcionNoExiste("No se encontró un usuario con esos datos."));

        // Generamos el código de 6 dígitos sin espacios
        String codigo = serviceUsuario.generarCodigoRecuperacion();

        // Guardamos el token (limpiando tokens previos del mismo usuario si existen)
        tokenRepo.deleteByUsuario(usuario); 
        
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(codigo);
        resetToken.setUsuario(usuario);
        resetToken.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));
        tokenRepo.save(resetToken);

        // Enviamos el correo usando el servicio que ya tienes
        emailService.enviarCorreoCodigo(usuario.getCorreo(), codigo);
    }

    @Override
    public boolean validarCodigo(String correo, String codigo) throws ExcepcionNoExiste {
        String codigoLimpio = codigo.trim(); // Eliminamos espacios accidentales
        Optional<PasswordResetToken> tokenOpt = tokenRepo.findByTokenAndUsuarioCorreo(codigoLimpio, correo);

        if (tokenOpt.isEmpty()) {
            return false;
        }

        PasswordResetToken token = tokenOpt.get();
        // Verificamos si ya expiró
        return token.getFechaExpiracion().isAfter(LocalDateTime.now());
    }

    @Override
    @Transactional
    public void cambiarContrasenia(String correo, String codigo, String nuevaPassword) throws ExcepcionNoExiste {
        if (!validarCodigo(correo, codigo)) {
            throw new IllegalArgumentException("El código es inválido o ha expirado.");
        }

        EntidadUsuario usuario = usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new ExcepcionNoExiste("Usuario no encontrado"));

        // Encriptamos y actualizamos
        usuario.setPassword_hash(passwordEncoder.encode(nuevaPassword));
        usuarioRepo.save(usuario);

        tokenRepo.deleteByUsuario(usuario);
    }
}
