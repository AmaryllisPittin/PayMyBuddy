package com.PayMyBuddy.pay_my_buddy.ServicesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.PayMyBuddy.pay_my_buddy.DTO.AddConnectionRequestDTO;
import com.PayMyBuddy.pay_my_buddy.DTO.UserConnectionResponseDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.UserConnectionEntity;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Mapper.UserConnectionMapper;
import com.PayMyBuddy.pay_my_buddy.Repository.UserConnectionRepository;
import com.PayMyBuddy.pay_my_buddy.Repository.UserRepository;
import com.PayMyBuddy.pay_my_buddy.Service.UserConnectionService;

@ExtendWith(MockitoExtension.class)
public class UserConnectionServiceTests {

    @Mock
    private UserConnectionRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConnectionMapper mapper;

    @InjectMocks
    private UserConnectionService service;

    @Test
    void addConnection_shouldSaveConnection_whenConnectionDoesNotExist() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        UserEntity connectedUser = new UserEntity();
        connectedUser.setId(2L);

        AddConnectionRequestDTO dto = new AddConnectionRequestDTO();
        dto.setConnectedUserId(2L);

        UserConnectionEntity entity = new UserConnectionEntity();

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(connectedUser));
        when(repository.existsByUser_IdAndConnectedUser_Id(1L, 2L)).thenReturn(false);
        when(mapper.toEntity(user, connectedUser)).thenReturn(entity);

        service.addConnection("user@test.com", dto);

        verify(repository).save(entity);

    }

    @Test
    void addConnection_shouldSaveConnection_whenConnectionAlreadyExist() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        UserEntity connectedUser = new UserEntity();
        connectedUser.setId(2L);

        AddConnectionRequestDTO dto = new AddConnectionRequestDTO();
        dto.setConnectedUserId(2L);

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(connectedUser));
        when(repository.existsByUser_IdAndConnectedUser_Id(1L, 2L)).thenReturn(true);

        service.addConnection("user@test.com", dto);

        verify(repository, never()).save(any());
        verify(mapper, never()).toEntity(any(), any());

    }

    @Test
    void addConnection_shouldThrowException_whenCurrentUserNotFound() {

        AddConnectionRequestDTO dto = new AddConnectionRequestDTO();
        dto.setConnectedUserId(2L);

        when(userRepository.findByEmail("sender@test.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.addConnection("sender@test.com", dto));

        assertEquals("l'Utilisateur est introuvable.", exception.getMessage());
        verify(repository, never()).save(any());

    }

    @Test
    void addConnection_shouldThrowException_whenConnectedUserNotFound() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        AddConnectionRequestDTO dto = new AddConnectionRequestDTO();
        dto.setConnectedUserId(2L);

        when(userRepository.findByEmail("sender@test.com")).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.addConnection("sender@test.com", dto));

        assertEquals("l'Utilisateur bénéficiaire est introuvable.", exception.getMessage());
        verify(repository, never()).save(any());

    }

    @Test
    void getConnections_shouldReturnConnectionDtos() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        UserConnectionEntity entity = new UserConnectionEntity();

        UserConnectionResponseDTO responseDTO = new UserConnectionResponseDTO();

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(repository.findByUser_Id(1L)).thenReturn(List.of(entity));
        when(mapper.toResponseDto(entity)).thenReturn(responseDTO);

        List<UserConnectionResponseDTO> result = service.getConnections("user@test.com");

        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));

    }

    @Test
    void addConnectionByMail_shouldFindRelationAndAddConnection() {

        UserEntity currentUser = new UserEntity();
        currentUser.setId(1L);

        UserEntity relation = new UserEntity();
        relation.setId(2L);

        UserConnectionEntity entity = new UserConnectionEntity();

        when(userRepository.findByEmail("relation@test.com")).thenReturn(Optional.of(relation));
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(relation));
        when(repository.existsByUser_IdAndConnectedUser_Id(1L, 2L))
                .thenReturn(false);
        when(mapper.toEntity(currentUser, relation)).thenReturn(entity);

        service.addConnectionByEmail("user@test.com", "relation@test.com");

        verify(repository).save(entity);

    }

    @Test
    void removeConnection_shouldDeleteConnection() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));

        service.removeConnection("user@test.com", 2L);

        verify(repository).deleteByUser_IdAndConnectedUser_Id(1L, 2L);

    }

}
