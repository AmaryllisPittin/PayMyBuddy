package com.PayMyBuddy.pay_my_buddy.Mapper;

import com.PayMyBuddy.pay_my_buddy.DTO.RegisterRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(RegisterRequestDTO dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(dto.username);
        userEntity.setEmail(dto.email);
        userEntity.setPassword(dto.password);
        return userEntity;
    }

}
