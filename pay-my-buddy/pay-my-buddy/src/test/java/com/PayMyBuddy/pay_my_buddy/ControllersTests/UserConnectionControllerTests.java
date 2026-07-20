package com.PayMyBuddy.pay_my_buddy.ControllersTests;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.PayMyBuddy.pay_my_buddy.Controllers.UserConnectionController;
import com.PayMyBuddy.pay_my_buddy.DTO.AddConnectionRequestDTO;
import com.PayMyBuddy.pay_my_buddy.DTO.UserConnectionResponseDTO;
import com.PayMyBuddy.pay_my_buddy.Service.UserConnectionService;

@ExtendWith(MockitoExtension.class)
public class UserConnectionControllerTests {

    @Mock
    private UserConnectionService service;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserConnectionController controller;

    private static final String CURRENT_USER_EMAIL = "alice@test.com";

    @BeforeEach
    void setUp() {

        when(authentication.getName()).thenReturn(CURRENT_USER_EMAIL);

    }

    @Test
    void addConnection_shouldCallServiceWithAuthenticatedUserAndDto() {

        AddConnectionRequestDTO dto = new AddConnectionRequestDTO();

        controller.addConnection(dto, authentication);

        verify(authentication).getName();
        verify(service).addConnection(CURRENT_USER_EMAIL, dto);
        verifyNoMoreInteractions(service);

    }

    @Test
    void getConnections_shouldReturnConnectionsFromService() {

        List<UserConnectionResponseDTO> expectedConnections = List.of(
                mock(UserConnectionResponseDTO.class),
                mock(UserConnectionResponseDTO.class));

        when(service.getConnections(CURRENT_USER_EMAIL))
                .thenReturn(expectedConnections);

        List<UserConnectionResponseDTO> result = controller.getConnections(authentication);

        assertSame(expectedConnections, result);

        verify(authentication).getName();
        verify(service).getConnections(CURRENT_USER_EMAIL);
        verifyNoMoreInteractions(service);

    }

}
