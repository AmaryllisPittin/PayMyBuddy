package com.PayMyBuddy.pay_my_buddy.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.PayMyBuddy.pay_my_buddy.DTO.AddConnectionRequestDTO;
import com.PayMyBuddy.pay_my_buddy.DTO.UserConnectionResponseDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.UserConnectionEntity;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Mapper.UserConnectionMapper;
import com.PayMyBuddy.pay_my_buddy.Repository.UserConnectionRepository;
import com.PayMyBuddy.pay_my_buddy.Repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserConnectionService {

    private final UserConnectionRepository repository;
    private final UserRepository userRepository;
    private final UserConnectionMapper mapper;

    @Transactional
    public void addConnection(String currentUserEmail, AddConnectionRequestDTO dto) {

        UserEntity user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("l'Utilisateur est introuvable."));

        UserEntity connectedUser = userRepository.findById(dto.getConnectedUserId())
                .orElseThrow(() -> new RuntimeException("l'Utilisateur bénéficiaire est introuvable."));

        if (repository.existsByUser_IdAndConnectedUser_Id(user.getId(), connectedUser.getId())) {
            return;
        }

        UserConnectionEntity entity = mapper.toEntity(user, connectedUser);

        repository.save(entity);
    }

    public List<UserConnectionResponseDTO> getConnections(String currentUserEmail) {
        UserEntity user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("l'Utilisateur est introuvable."));

        return repository.findByUser_Id(user.getId())
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Transactional
    public void removeConnection(String currentUserEmail, Long connectedUserId) {

        UserEntity user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

        repository.deleteByUser_IdAndConnectedUser_Id(user.getId(), connectedUserId);
    }

    public void addConnectionByEmail(String currentUserEmail, String relationEmail) {

        UserEntity relation = userRepository.findByEmail(relationEmail)
                .orElseThrow(() -> new RuntimeException("Aucun utilisateur trouvé avec cet email."));

        AddConnectionRequestDTO dto = new AddConnectionRequestDTO();
        dto.setConnectedUserId(relation.getId());
        addConnection(currentUserEmail, dto);
    }

}
