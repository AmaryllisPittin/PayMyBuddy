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

/**
 * Service chargé de la gestion des transferts d'argent entre utilisateurs
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

        private final TransactionRepository transactionRepository;
        private final UserRepository userRepository;
        private final UserConnectionRepository userConnectionRepository;

        @Transactional
        public void createTransaction(String senderEmail, TransfertRequestDTO dto) {
                // Récupération de l'expéditeur et du bénéficiaire
                UserEntity sender = userRepository.findByEmail(senderEmail)
                                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

                UserEntity receiver = userRepository.findById(dto.getConnectedUserId())
                                .orElseThrow(() -> new RuntimeException("Bénéficiaire introuvable."));
                // Vérifie que la relation existe entre l'expéditeur et le bénéficiaire
                boolean connectionExists = userConnectionRepository
                                .existsByUser_IdAndConnectedUser_Id(sender.getId(), receiver.getId());

                if (!connectionExists) {
                        throw new RuntimeException("Ce bénéficiaire n'est pas dans vos relations.");
                }
                // Vérifie que le solde est insuffisant
                if (sender.getBalance().compareTo(dto.getAmount()) < 0) {
                        throw new RuntimeException("Solde insuffisant.");
                }
                // Met à jour le solde des utilisateurs
                sender.setBalance(sender.getBalance().subtract(dto.getAmount()));

                receiver.setBalance(receiver.getBalance().add(dto.getAmount()));

                TransactionEntity transaction = TransactionEntity.builder()
                                .sender(sender)
                                .receiver(receiver)
                                .description(dto.getDescription())
                                .amount(dto.getAmount())
                                .build();
                // Enregistre la transaction
                transactionRepository.save(transaction);

        }

        /**
         * Retourne l'historique des transactions de l'utilisateur connecté
         */
        public List<TransactionEntity> findTransactionsByUserEmail(String email) {

                UserEntity user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable."));

                return transactionRepository.findBySender_IdOrderByIdDesc(user.getId());

        }

}
