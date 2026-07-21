package com.PayMyBuddy.pay_my_buddy.ControllersTests;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.PayMyBuddy.pay_my_buddy.Controllers.UserController;
import com.PayMyBuddy.pay_my_buddy.DTO.RegisterRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Service.AuthService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserController controller;

    @Test
    void register_ShouldCallAuthService() {

        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("Ben");
        dto.setEmail("ben@test.com");
        dto.setPassword("password");

        controller.register(dto);

        verify(authService).register(dto);

    }

}
