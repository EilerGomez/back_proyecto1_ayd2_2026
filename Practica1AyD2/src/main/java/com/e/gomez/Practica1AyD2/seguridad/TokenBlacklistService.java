/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.seguridad;

/**
 *
 * @author eiler
 */
import java.util.Date;

public interface TokenBlacklistService {
    void blacklist(String jti, Date expiresAt);
    boolean isBlacklisted(String jti);
}
