package com.PayMyBuddy.pay_my_buddy.Service;

import org.springframework.stereotype.Service;

import com.PayMyBuddy.pay_my_buddy.DTO.UserConnectionDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.UserConnectionEntity;
import com.PayMyBuddy.pay_my_buddy.Entity.UserConnectionId;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Mapper.UserConnectionMapper;
import com.PayMyBuddy.pay_my_buddy.Repository.UserConnectionRepository;
import com.PayMyBuddy.pay_my_buddy.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserConnectionService {

    private final UserConnectionRepository repository;
    private final UserRepository userRepository;
    private final UserConnectionMapper mapper;

    public void addConnection(UserConnectionDTO dto) {

        if (repository.existsByUser_IdAndConnectedUser_Id(dto.userId, dto.connectedUserId)) {
            return;
        }

        UserEntity user = userRepository.findById(dto.userId)
                .orElseThrow(() -> new RuntimeException("l'Utilisateur est introuvable."));

        UserEntity connectedUser = userRepository.findById(dto.connectedUserId)
                .orElseThrow(() -> new RuntimeException("l'Utilisateur connecté est introuvable."));

        UserConnectionEntity entity = mapper.toEntity(dto, user, connectedUser);

        repository.save(entity);

    }

    public List<UserConnectionDTO> getConnections(Long userId) {

        return repository.findAll()
                .stream()
                .filter(c -> c.getId().getUserId().equals(userId))
                .map(mapper::toDto)
                .collect(Collectors.toList());

    }

    public void removeConnection(Long userId, Long connectedUserId) {
        repository.deleteById(new UserConnectionId(userId, connectedUserId));
    }

}
