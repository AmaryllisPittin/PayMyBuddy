package com.PayMyBuddy.pay_my_buddy.Service;

import org.springframework.security.core.Authentication;
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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void register(RegisterRequestDTO dto) {

        if (userRepository.existsByEmail(dto.email)) {
            throw new RuntimeException("Cet email est déjà utilisé.");
        }

        if (userRepository.existsByUsername(dto.username)) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà utilisé.");
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.username);
        user.setEmail(dto.email);
        user.setPassword(passwordEncoder.encode(dto.password));

        userRepository.save(user);

    }

    public Authentication login(LoginRequestDTO dto) {

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()));

    }

}
