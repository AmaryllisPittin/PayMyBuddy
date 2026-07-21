package com.PayMyBuddy.pay_my_buddy.Controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.PayMyBuddy.pay_my_buddy.DTO.RegisterRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Service.AuthService;

/**
 * 
 * API REST chargée de l'inscription des nouveaux utilisateurs
 */
@RestController
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequestDTO dto) {

        System.out.println("DTO username = " + dto.username);
        System.out.println("DTO email = " + dto.email);
        System.out.println("DTO password = " + dto.password);

        authService.register(dto);
    }

}
