/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.seguridad;

/**
 *
 * @author eiler
 */
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryTokenBlacklistService implements TokenBlacklistService {

    // jti -> expiresAtMillis
    private final Map<String, Long> store = new ConcurrentHashMap<>();

    @Override
    public void blacklist(String jti, Date expiresAt) {
        store.put(jti, expiresAt.getTime());
    }

    @Override
    public boolean isBlacklisted(String jti) {
        Long exp = store.get(jti);
        if (exp == null) return false;

        // limpieza simple
        if (System.currentTimeMillis() > exp) {
            store.remove(jti);
            return false;
        }
        return true;
    }
}
