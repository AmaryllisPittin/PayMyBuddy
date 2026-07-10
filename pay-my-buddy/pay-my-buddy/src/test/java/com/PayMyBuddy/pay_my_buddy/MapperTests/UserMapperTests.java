package com.PayMyBuddy.pay_my_buddy.MapperTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.PayMyBuddy.pay_my_buddy.DTO.RegisterRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Mapper.UserMapper;

public class UserMapperTests {

    @Test
    void toEntity_shouldMapRegisterRequestDtoToUserEntity() {

        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setUsername("Alice");
        dto.setEmail("alice@test.com");
        dto.setPassword("password");

        UserEntity result = UserMapper.toEntity(dto);

        assertNotNull(result);
        assertEquals("Alice", result.getUsername());
        assertEquals("alice@test.com", result.getEmail());
        assertEquals("password", result.getPassword());

    }

}
