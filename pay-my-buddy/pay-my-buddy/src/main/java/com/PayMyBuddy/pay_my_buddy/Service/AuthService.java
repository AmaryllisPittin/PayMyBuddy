package com.PayMyBuddy.pay_my_buddy.Service;

import org.springframework.security.core.Authentication;

import java.math.BigDecimal;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.PayMyBuddy.pay_my_buddy.DTO.LoginRequestDTO;
import com.PayMyBuddy.pay_my_buddy.DTO.RegisterRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service chargé de l'authentification et de l'inscription des utilisateurs
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void register(RegisterRequestDTO dto) {
        // Vérifie que le mail + nom utilisateur existent
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé.");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà utilisé.");
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        // Hache le mot de passe avant son enregistrement
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        // Version PROTOTYPE - Initialisation choisie pour le nouvel utilisateur.
        user.setBalance(new BigDecimal("300.00"));

        userRepository.save(user);

    }

    /**
     * Authentification d'un utilisateur à partir de ses identifiants
     */

    public Authentication login(LoginRequestDTO dto) {

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()));

    }

}
