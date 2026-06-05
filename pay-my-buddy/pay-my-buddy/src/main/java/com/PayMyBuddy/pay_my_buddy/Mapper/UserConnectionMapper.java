package com.PayMyBuddy.pay_my_buddy.Mapper;

import org.springframework.stereotype.Component;

import com.PayMyBuddy.pay_my_buddy.DTO.UserConnectionResponseDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.UserConnectionEntity;
import com.PayMyBuddy.pay_my_buddy.Entity.UserConnectionId;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;

@Component
public class UserConnectionMapper {

    public UserConnectionResponseDTO toResponseDto(UserConnectionEntity entity) {

        UserEntity connectedUser = entity.getConnectedUser();
        return new UserConnectionResponseDTO(
                connectedUser.getId(),
                connectedUser.getUsername(),
                connectedUser.getEmail());
    }

    public UserConnectionEntity toEntity(
            UserEntity user,
            UserEntity connectedUser) {

        UserConnectionEntity entity = new UserConnectionEntity();

        UserConnectionId id = new UserConnectionId(
                user.getId(),
                connectedUser.getId());

        entity.setId(id);
        entity.setUser(user);
        entity.setConnectedUser(connectedUser);

        return entity;

    }

}
