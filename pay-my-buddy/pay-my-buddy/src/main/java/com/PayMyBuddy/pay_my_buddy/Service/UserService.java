package com.PayMyBuddy.pay_my_buddy.Service;

import org.springframework.stereotype.Service;

import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * 
 * Service chargé de la gestion des utilisateurs: inscription et recherche
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Recherche d'un utilisateur à partir de son adresse mail
     */

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
    }

}
