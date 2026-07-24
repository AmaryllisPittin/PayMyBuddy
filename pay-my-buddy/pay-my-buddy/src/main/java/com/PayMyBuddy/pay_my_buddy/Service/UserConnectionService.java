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
import lombok.extern.slf4j.Slf4j;

/**
 * Service chargé de la gestion des relations entre les utilisateurs
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserConnectionService {

    private final UserConnectionRepository repository;
    private final UserRepository userRepository;
    private final UserConnectionMapper mapper;

    @Transactional
    public void addConnection(String currentUserEmail, AddConnectionRequestDTO dto) {

        log.info("Début de l'ajout d'une relation pour l'utilisateur : {}", currentUserEmail);

        // Récupère l'utilisateur connecté et la relation à ajouter
        UserEntity user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("l'Utilisateur est introuvable."));

        log.info("Utilisateur connecté trouvé avec l'identifiant : {}", user.getId());

        UserEntity connectedUser = userRepository.findById(dto.getConnectedUserId())
                .orElseThrow(() -> new RuntimeException("l'Utilisateur bénéficiaire est introuvable."));

        log.info("Utilisateur à ajouter trouvé avec l'identifiant : {}", connectedUser.getId());

        // Vérifie si la relation n'existe pas déjà
        if (repository.existsByUser_IdAndConnectedUser_Id(user.getId(), connectedUser.getId())) {

            return;
        }

        // Créé et enregistre la nouvelle relation
        UserConnectionEntity entity = mapper.toEntity(user, connectedUser);

        repository.save(entity);

        log.warn("Ajout ignoré: la relation existe déjà entre les utilisateur {} et {}", user.getId(),
                connectedUser.getId());
    }

    /**
     * Retourne la liste des relations de l'utilisateur connecté
     */

    public List<UserConnectionResponseDTO> getConnections(String currentUserEmail) {

        log.info("Recherche des relations de l'utilisateur : {}", currentUserEmail);

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

        log.info("Début de la suppression de la relation avec l'utilisateur {}.", connectedUserId);

        UserEntity user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

        repository.deleteByUser_IdAndConnectedUser_Id(user.getId(), connectedUserId);

        log.info("Relation supprimée entre les utilisateurs {} et {}.", user.getId(), connectedUserId);
    }

    /**
     * Ajout d'une relation à partir de son adresse mail
     */

    public void addConnectionByEmail(String currentUserEmail, String relationEmail) {

        log.info("Recherche d'un utilisateur à partir de son adresse email.");

        UserEntity relation = userRepository.findByEmail(relationEmail)
                .orElseThrow(() -> new RuntimeException("Aucun utilisateur trouvé avec cet email."));

        log.info("Utilisateur trouvé avec l'identifiant : {}.", relation.getId());

        AddConnectionRequestDTO dto = new AddConnectionRequestDTO();
        dto.setConnectedUserId(relation.getId());
        addConnection(currentUserEmail, dto);
    }

}
