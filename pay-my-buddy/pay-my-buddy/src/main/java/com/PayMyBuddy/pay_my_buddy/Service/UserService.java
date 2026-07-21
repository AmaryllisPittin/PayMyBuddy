package com.PayMyBuddy.pay_my_buddy.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Repository.UserRepository;

/**
 * 
 * Service chargé de la gestion des utilisateurs: inscription et recherche
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Inscription d'un nouvel utilisateur après vérification du mail + hachage du
     * mot de passe
     */
    public void register(UserEntity userEntity) {
        if (userRepository.existsByEmail(userEntity.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé.");
        }
        String hashedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(hashedPassword);
        userRepository.save(userEntity);
    }

    /**
     * Vérification des identifiants d'un utilisateur, retourne ses informations
     */
    public UserEntity login(String email, String password) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("l'utilisateur est introuvable."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Mot de passe invalide.");
        }

        return user;

    }

    /**
     * Recherche d'un utilisateur à partir de son adresse mail
     */

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));
    }

}
