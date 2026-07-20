package com.PayMyBuddy.pay_my_buddy.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * Controller chargé d'afficher le profil de l'utilisateur connecté
 */
@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profilePage(Model model, Authentication authentication) {
        // Récupère l'adresse mail d el'utilisateur connecté
        String email = authentication.getName();
        // Charge les informations d el'utilisateur depuis la base de données
        UserEntity user = userService.findByEmail(email);
        // Transmet les données au Thymeleaf
        model.addAttribute("user", user);
        return "profile";
    }

}
