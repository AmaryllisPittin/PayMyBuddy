package com.PayMyBuddy.pay_my_buddy.Controllers;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PayMyBuddy.pay_my_buddy.DTO.LoginRequestDTO;
import com.PayMyBuddy.pay_my_buddy.DTO.RegisterRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;

/**
 * Controller chargé de l'authentification des utilisateurs - inscription et
 * connexion
 **/
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    // Traite la création de compte des nouveaux utilisateurs
    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequestDTO dto) {
        service.register(dto);
        return "redirect:/login";
    }

    // Authentifie l'utilisateur et ouvre une session sécurisée
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequestDTO dto, HttpServletRequest request) {

        System.out.println("LOGIN EMAIL: " + dto.getEmail());
        System.out.println("LOGIN PASSWORD: " + dto.getPassword());

        Authentication authentication = service.login(dto);
        // Enregistre l'utilisateur authentifié dans le contexte Spring Security
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        // Conserve le contexte de sécurité dans la session HTTP
        request.getSession(true)
                .setAttribute(
                        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        context);

        return "redirect:/profile";
    }
}
