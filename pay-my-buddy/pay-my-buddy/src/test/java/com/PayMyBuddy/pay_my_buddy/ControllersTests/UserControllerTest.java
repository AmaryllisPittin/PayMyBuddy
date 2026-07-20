package com.PayMyBuddy.pay_my_buddy.ControllersTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.PayMyBuddy.pay_my_buddy.Controllers.UserController;
import com.PayMyBuddy.pay_my_buddy.DTO.RegisterRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    @Test
    void register_ShouldCallUserService() {

        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("Ben");
        dto.setEmail("ben@test.com");
        dto.setPassword("password");

        controller.register(dto);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);

        verify(userService).register(captor.capture());

        UserEntity user = captor.getValue();

        assertEquals("Ben", user.getUsername());
        assertEquals("ben@test.com", user.getEmail());
        assertEquals("password", user.getPassword());

    }

}
