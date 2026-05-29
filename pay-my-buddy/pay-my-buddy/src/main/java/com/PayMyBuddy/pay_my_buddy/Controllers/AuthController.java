package com.PayMyBuddy.pay_my_buddy.Controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.PayMyBuddy.pay_my_buddy.DTO.LoginRequestDTO;
import com.PayMyBuddy.pay_my_buddy.DTO.RegisterRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Service.AuthService;

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
    public String login(@RequestBody LoginRequestDTO dto) {
        service.login(dto);
        return "Connexion réussie";
    }
}
