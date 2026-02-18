package com.e.gomez.Practica1AyD2.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretBase64;

    @Value("${jwt.expiration:3600000}")
    private long expirationMs;

    private SecretKey key;

    @PostConstruct
    private void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretBase64);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Integer userId, String username, String rol) {

        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        String jti = UUID.randomUUID().toString(); // ← IMPORTANTE

        return Jwts.builder()
                .id(jti) // ← AQUÍ SE GUARDA EL JTI
                .subject(username)
                .issuedAt(now)
                .expiration(exp)
                .claim("uid", userId)
                .claim("rol", rol)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 🔥 ESTE ES EL QUE TE FALTABA
    public String getJti(String token) {
        return parseClaims(token).getId();
    }

    public Integer getUserId(String token) {
        Object uid = parseClaims(token).get("uid");
        return (uid instanceof Integer i)
                ? i
                : Integer.valueOf(uid.toString());
    }

    public String getRol(String token) {
        return parseClaims(token).get("rol", String.class);
    }

    public long getExpirationEpochMs(String token) {
        return parseClaims(token).getExpiration().getTime();
    }

    public long getExpirationMs() {
        return expirationMs;
    }
}
