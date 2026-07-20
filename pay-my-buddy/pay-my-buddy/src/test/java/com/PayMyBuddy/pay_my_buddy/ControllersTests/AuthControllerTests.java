package com.PayMyBuddy.pay_my_buddy.ControllersTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.PayMyBuddy.pay_my_buddy.Controllers.AuthController;
import com.PayMyBuddy.pay_my_buddy.DTO.LoginRequestDTO;
import com.PayMyBuddy.pay_my_buddy.DTO.RegisterRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {

    @Mock
    private AuthService service;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthController authController;

    @AfterEach
    void cleanSecurityContext() {

        SecurityContextHolder.clearContext();

    }

    @Test
    void register_shouldRegisterUserAndRedirectToLogin() {

        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("Ben");
        dto.setEmail("ben@test.com");
        dto.setPassword("password");

        String result = authController.register(dto);

        assertEquals("redirect:/login", result);
        verify(service).register(dto);

    }

    @Test
    void login_shouldAuthenticateStoreContextInSessionAndRedirectToProfile() {

        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("ben@test.com");
        dto.setPassword("password");

        Authentication authentication = mock(Authentication.class);

        when(service.login(dto)).thenReturn(authentication);
        when(request.getSession(true)).thenReturn(session);

        String result = authController.login(dto, request);

        assertEquals("redirect:/profile", result);

        verify(service).login(dto);
        verify(request).getSession(true);

        SecurityContext context = SecurityContextHolder.getContext();

        assertNotNull(context);
        assertEquals(authentication, context.getAuthentication());

        verify(session).setAttribute(
                eq(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY), same(context));

    }

}
