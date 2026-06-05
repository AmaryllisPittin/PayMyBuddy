package com.PayMyBuddy.pay_my_buddy.Controllers;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.PayMyBuddy.pay_my_buddy.DTO.LoginRequestDTO;
import com.PayMyBuddy.pay_my_buddy.DTO.RegisterRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequestDTO dto) {
        service.register(dto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO dto, HttpServletRequest request) {

        Authentication authentication = service.login(dto);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        request.getSession(true)
                .setAttribute(
                        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        context);

        return "Connexion réussie";
    }
}
