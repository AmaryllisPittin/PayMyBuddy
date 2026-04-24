package com.PayMyBuddy.pay_my_buddy.Service;

import org.springframework.stereotype.Service;

import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(UserEntity userEntity) {
        if (userRepository.existsByEmail(userEntity.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé.");
        }
        userRepository.save(userEntity);
    }

    public UserEntity login(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("l'utilisateur est introuvable."));
    }

}
