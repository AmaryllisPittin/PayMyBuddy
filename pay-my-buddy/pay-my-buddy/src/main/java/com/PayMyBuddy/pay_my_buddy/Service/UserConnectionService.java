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

/**
 * Service chargé de la gestion des relations entre les utilisateurs
 */
@Service
@RequiredArgsConstructor
public class UserConnectionService {

    private final UserConnectionRepository repository;
    private final UserRepository userRepository;
    private final UserConnectionMapper mapper;

    @Transactional
    public void addConnection(String currentUserEmail, AddConnectionRequestDTO dto) {
        // Récupère l'utilisateur connecté et la relation à ajouter
        UserEntity user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("l'Utilisateur est introuvable."));

        UserEntity connectedUser = userRepository.findById(dto.getConnectedUserId())
                .orElseThrow(() -> new RuntimeException("l'Utilisateur bénéficiaire est introuvable."));
        // Vérifie si la relation n'existe pas déjà
        if (repository.existsByUser_IdAndConnectedUser_Id(user.getId(), connectedUser.getId())) {
            return;
        }
        // Créé et enregistre la nouvelle relation
        UserConnectionEntity entity = mapper.toEntity(user, connectedUser);

        repository.save(entity);
    }

    /**
     * Retourne la liste des relations de l'utilisateur connecté
     */

    public List<UserConnectionResponseDTO> getConnections(String currentUserEmail) {
        UserEntity user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("l'Utilisateur est introuvable."));

        return repository.findByUser_Id(user.getId())
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    /**
     * Suppression d'une relation
     */

    @Transactional
    public void removeConnection(String currentUserEmail, Long connectedUserId) {

        UserEntity user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

        repository.deleteByUser_IdAndConnectedUser_Id(user.getId(), connectedUserId);
    }

    /**
     * Ajout d'une relation à partir de son adresse mail
     */

    public void addConnectionByEmail(String currentUserEmail, String relationEmail) {

        UserEntity relation = userRepository.findByEmail(relationEmail)
                .orElseThrow(() -> new RuntimeException("Aucun utilisateur trouvé avec cet email."));

        AddConnectionRequestDTO dto = new AddConnectionRequestDTO();
        dto.setConnectedUserId(relation.getId());
        addConnection(currentUserEmail, dto);
    }

}
