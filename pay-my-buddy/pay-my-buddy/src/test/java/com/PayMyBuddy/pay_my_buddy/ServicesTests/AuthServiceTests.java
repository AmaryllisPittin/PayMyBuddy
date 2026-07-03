package com.PayMyBuddy.pay_my_buddy.ServicesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.PayMyBuddy.pay_my_buddy.DTO.LoginRequestDTO;
import com.PayMyBuddy.pay_my_buddy.DTO.RegisterRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Repository.UserRepository;
import com.PayMyBuddy.pay_my_buddy.Service.AuthService;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {

        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("Ben");
        dto.setEmail("ben@test.com");
        dto.setPassword("password");

        when(userRepository.existsByEmail("ben@test.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.register(dto));

        assertEquals("Cet email est déjà utilisé.", exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));

    }

    @Test
    void register_shouldThrowException_whenUsernameAlreadyExists() {

        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("Ben");
        dto.setEmail("ben@test.com");
        dto.setPassword("password");

        when(userRepository.existsByUsername("Ben")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.register(dto));

        assertEquals("Ce nom d'utilisateur est déjà utilisé.", exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));

    }

    @Test
    void register_shouldSaveUserWithEncodedPasswordAndDefaultBalance() {

        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("Ben");
        dto.setEmail("ben@test.com");
        dto.setPassword("password");

        when(userRepository.existsByUsername("Ben")).thenReturn(false);
        when(userRepository.existsByEmail("ben@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        authService.register(dto);

        ArgumentCaptor<UserEntity> useCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(useCaptor.capture());

        UserEntity savedUser = useCaptor.getValue();

        assertEquals("Ben", savedUser.getUsername());
        assertEquals("ben@test.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(new BigDecimal("300.00"), savedUser.getBalance());

    }

    @Test
    void login_shouldAuthenticateUser() {

        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("ben@test.com");
        dto.setPassword("password");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        Authentication result = authService.login(dto);

        assertEquals(authentication, result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

    }

}
