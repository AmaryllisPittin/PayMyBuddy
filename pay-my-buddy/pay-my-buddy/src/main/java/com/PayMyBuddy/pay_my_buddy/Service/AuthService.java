package com.PayMyBuddy.pay_my_buddy.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
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

    public boolean login(LoginRequestDTO dto) {

        UserEntity user = userRepository.findByEmail(dto.email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return passwordEncoder.matches(dto.password, user.getPassword());

    }

}
