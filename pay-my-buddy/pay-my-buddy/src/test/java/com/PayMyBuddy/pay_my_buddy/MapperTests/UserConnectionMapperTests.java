package com.PayMyBuddy.pay_my_buddy.MapperTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.PayMyBuddy.pay_my_buddy.DTO.UserConnectionResponseDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.UserConnectionEntity;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Mapper.UserConnectionMapper;

public class UserConnectionMapperTests {

    private final UserConnectionMapper mapper = new UserConnectionMapper();

    @Test
    void toResponseDto_shouldMapEntityToResponseDto() {

        UserEntity connectedUser = new UserEntity();
        connectedUser.setId(2L);
        connectedUser.setUsername("Alice");
        connectedUser.setEmail("alice@test.com");

        UserConnectionEntity entity = new UserConnectionEntity();
        entity.setConnectedUser(connectedUser);

        UserConnectionResponseDTO dto = mapper.toResponseDto(entity);

        assertNotNull(dto);
        assertEquals(2L, dto.getConnectedUserId());
        assertEquals("Alice", dto.getUsername());
        assertEquals("alice@test.com", dto.getEmail());

    }

    @Test
    void toEntity_shouldCreateConnectionEntity() {

        UserEntity user = new UserEntity();
        user.setId(1L);

        UserEntity connectedUser = new UserEntity();
        connectedUser.setId(2L);

        UserConnectionEntity entity = mapper.toEntity(user, connectedUser);

        assertNotNull(entity);

        assertEquals(user, entity.getUser());
        assertEquals(connectedUser, entity.getConnectedUser());

        assertNotNull(entity.getId());
        assertEquals(1L, entity.getId().getUserId());
        assertEquals(2L, entity.getId().getConnectedUserId());

    }

}
