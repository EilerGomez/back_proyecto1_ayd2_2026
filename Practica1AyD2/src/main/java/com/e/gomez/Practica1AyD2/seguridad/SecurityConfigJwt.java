/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.seguridad;

/**
 *
 * @author eiler
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfigJwt {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfigJwt(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        

        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers(
                        "/v1/auth/login",
                        "/v1/auth/logout",
                        "/v1/auth/recuperacion/**"
                ).permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/usuarios").permitAll()//crear cuenta
                .requestMatchers(HttpMethod.GET, "/v1/roles").permitAll()//traer roles para crear cuenta.
                .requestMatchers("/v1/reportes-admin/**").permitAll()
                .requestMatchers("/v1/revistas/**").hasAnyRole("ADMIN", "EDITOR","SUSCRIPTOR")
                .anyRequest().authenticated()
        );

        http.httpBasic(Customizer.withDefaults());

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
    
    
}
