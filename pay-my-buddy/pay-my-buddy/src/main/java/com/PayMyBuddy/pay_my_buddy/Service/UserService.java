package com.PayMyBuddy.pay_my_buddy.Service;

import org.springframework.stereotype.Service;

import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Service chargé de la gestion des utilisateurs: inscription et recherche
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Recherche d'un utilisateur à partir de son adresse mail
     */

    public UserEntity findByEmail(String email) {

        log.info("Recherche de l'utilisateur avec l'adresse email : {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

    }

}
