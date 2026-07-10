package com.PayMyBuddy.pay_my_buddy.ServicesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Repository.UserRepository;
import com.PayMyBuddy.pay_my_buddy.Service.CustomUserDetailService;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {

        UserEntity user = new UserEntity();
        user.setEmail("user@test.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailService.loadUserByUsername("user@test.com");

        assertNotNull(result);
        assertEquals("user@test.com", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());

        assertTrue(
                result.getAuthorities()
                        .stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));

        verify(userRepository).findByEmail("user@test.com");

    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExists() {

        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailService
                        .loadUserByUsername("unknown@test.com"));

        assertEquals("Utilisateur introuvable", exception.getMessage());

        verify(userRepository).findByEmail("unknown@test.com");

    }

}
