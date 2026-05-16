package com.PayMyBuddy.pay_my_buddy.Mapper;

import org.springframework.stereotype.Component;

import com.PayMyBuddy.pay_my_buddy.DTO.UserConnectionDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.UserConnectionEntity;
import com.PayMyBuddy.pay_my_buddy.Entity.UserConnectionId;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;

@Component
public class UserConnectionMapper {

    public UserConnectionDTO toDto(UserConnectionEntity entity) {

        UserConnectionDTO dto = new UserConnectionDTO();
        dto.userId = entity.getUser().getId();
        dto.connectedUserId = entity.getConnectedUser().getId();
        return dto;
    }

    public UserConnectionEntity toEntity(UserConnectionDTO dto, UserEntity user, UserEntity connectedUser) {

        UserConnectionEntity entity = new UserConnectionEntity();

        UserConnectionId id = new UserConnectionId();
        id.setUserId(dto.userId);
        id.setConnectedUserId(dto.connectedUserId);

        entity.setId(id);
        entity.setUser(user);
        entity.setConnectedUser(connectedUser);

        return entity;

    }

}
