package com.PayMyBuddy.pay_my_buddy.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.PayMyBuddy.pay_my_buddy.DTO.TransfertRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.TransactionEntity;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Repository.TransactionRepository;
import com.PayMyBuddy.pay_my_buddy.Repository.UserConnectionRepository;
import com.PayMyBuddy.pay_my_buddy.Repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service chargé de la gestion des transferts d'argent entre utilisateurs
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

        private final TransactionRepository transactionRepository;
        private final UserRepository userRepository;
        private final UserConnectionRepository userConnectionRepository;

        @Transactional
        public void createTransaction(String senderEmail, TransfertRequestDTO dto) {

                log.info("Début de la création d'une transaction pour un utilisateur: {}", senderEmail);

                // Récupération de l'expéditeur et du bénéficiaire
                UserEntity sender = userRepository.findByEmail(senderEmail)
                                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

                log.info("Expéditeur trouvé avec cet identifiant: {}", sender.getId());

                UserEntity receiver = userRepository.findById(dto.getConnectedUserId())
                                .orElseThrow(() -> new RuntimeException("Bénéficiaire introuvable."));

                log.info("Bénéficiaire trouvé avec cet identifiant: {}", receiver.getId());

                // Vérifie que la relation existe entre l'expéditeur et le bénéficiaire
                boolean connectionExists = userConnectionRepository
                                .existsByUser_IdAndConnectedUser_Id(sender.getId(), receiver.getId());

                log.info("Vérification de la relation entre l'expéditeur et le bénéficiaire.");

                if (!connectionExists) {
                        log.warn("Transaction refusée: le bénéficiaire n'est pas dans les relations de l'expéditeur.");
                        throw new RuntimeException("Ce bénéficiaire n'est pas dans vos relations.");
                }

                log.info("La relation avec le bénéficiaire a été vérifiée.");
                // Vérifie que le solde est insuffisant
                if (sender.getBalance().compareTo(dto.getAmount()) < 0) {
                        log.warn("Transaction refusée: le solde est insuffisant pour l'utilisateur {}", sender.getId());
                        throw new RuntimeException("Solde insuffisant.");
                }

                log.info("Le solde de l'expéditeur est suffisant.");

                // Met à jour le solde des utilisateurs
                sender.setBalance(sender.getBalance().subtract(dto.getAmount()));

                receiver.setBalance(receiver.getBalance().add(dto.getAmount()));

                log.info("Les soldes de l'expéditeur et du bénéficiaire ont été mis à jour.");

                TransactionEntity transaction = TransactionEntity.builder()
                                .sender(sender)
                                .receiver(receiver)
                                .description(dto.getDescription())
                                .amount(dto.getAmount())
                                .build();
                // Enregistre la transaction
                transactionRepository.save(transaction);

                log.info("Transaction enregistrée avec succès entre les utilisateurs {} et {}.", sender.getId(),
                                receiver.getId());

        }

        /**
         * Retourne l'historique des transactions de l'utilisateur connecté
         */
        public List<TransactionEntity> findTransactionsByUserEmail(String email) {

                log.info("Recherche de l'historique des transactions de l'utilisateur : {}", email);

                UserEntity user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

                log.info("Utilisateur trouvé, récupération de son historique de transactions.");

                return transactionRepository.findBySender_IdOrderByIdDesc(user.getId());

        }

}
