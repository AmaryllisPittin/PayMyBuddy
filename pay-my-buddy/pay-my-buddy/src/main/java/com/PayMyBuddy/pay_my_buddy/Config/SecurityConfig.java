package com.PayMyBuddy.pay_my_buddy.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.PayMyBuddy.pay_my_buddy.Service.CustomUserDetailService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    // Configure les règles de sécurité et d'accès aux routes de l'application
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                // Désactive la protection CSRF
                .csrf(csrf -> csrf.disable())
                // Défini le service utilisé pour charger les informations de l'utilisateur
                .userDetailsService(customUserDetailService)
                // Configure les règles d'accès aux routes et ressources
                .authorizeHttpRequests(auth -> auth
                        // Routes et ressources accessibles SANS authentification
                        .requestMatchers("/", "/login", "/register", "/auth/login", "/auth/register", "/css/style.css",
                                "/js/**", "/images/**", "/favicon.ico")
                        .permitAll()
                        // Autorise les requêtes HTTP OPTIONS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Toutes les aiutres routes nécessitent une authentification
                        .anyRequest().authenticated())
                // Désactive le formulaire de connexion par défaut de Spring Security
                .formLogin(form -> form.disable())
                // Désactive l'authentification HTTP Basic
                .httpBasic(httpBasic -> httpBasic.disable())
                .build();
    }

    // Gère l'authentification avec l'AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Configure BCrypt pour le hachage sécurisé des mots de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
