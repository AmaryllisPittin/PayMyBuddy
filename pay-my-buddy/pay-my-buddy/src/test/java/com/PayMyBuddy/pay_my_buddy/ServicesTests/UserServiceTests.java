package com.PayMyBuddy.pay_my_buddy.ServicesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Repository.UserRepository;
import com.PayMyBuddy.pay_my_buddy.Service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {

        UserEntity user = new UserEntity();
        user.setEmail("user@test.com");
        user.setPassword("password");

        when(userRepository.existsByEmail("user@test.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.register(user));

        assertEquals("Cet email est déjà utilisé.", exception.getMessage());
        verify(userRepository, never()).save(any());

    }

    @Test
    void register_shouldEncodePasswordAndSaveUser_whenEmailDoesNotExist() {

        UserEntity user = new UserEntity();
        user.setEmail("user@test.com");
        user.setPassword("password");

        when(userRepository.existsByEmail("user@test.com")).thenReturn(false);

        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        userService.register(user);

        assertEquals("hashedPassword", user.getPassword());
        verify(userRepository).save(user);

    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.login("user@test.com", "password"));

        assertEquals("l'utilisateur est introuvable.", exception.getMessage());

    }

    @Test
    void login_shouldThrowException_whenPasswordIsInvalid() {

        UserEntity user = new UserEntity();
        user.setEmail("user@test.com");
        user.setPassword("hashedPassword");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("badPassword", "hashedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.login("user@test.com", "badPassword"));

        assertEquals("Mot de passe invalide.", exception.getMessage());

    }

    @Test
    void login_shouldReturnUser_whenCredentialsAreValid() {

        UserEntity user = new UserEntity();
        user.setEmail("user@test.com");
        user.setPassword("hashedPassword");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);

        UserEntity result = userService.login("user@test.com", "password");

        assertEquals(user, result);

    }

    @Test
    void findByEmail_shouldReturnUser_whenUserExists() {

        UserEntity user = new UserEntity();
        user.setEmail("user@test.com");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        UserEntity result = userService.findByEmail("user@test.com");

        assertEquals(user, result);

    }

    @Test
    void findByEmail_shouldThrowException_whenUserNotFound() {

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.findByEmail("user@test.com"));

        assertEquals("Utilisateur introuvable.", exception.getMessage());

    }

}
