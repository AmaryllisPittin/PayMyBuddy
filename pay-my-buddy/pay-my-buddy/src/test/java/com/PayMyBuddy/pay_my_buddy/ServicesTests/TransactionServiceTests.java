package com.PayMyBuddy.pay_my_buddy.ServicesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.PayMyBuddy.pay_my_buddy.DTO.TransfertRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.TransactionEntity;
import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;
import com.PayMyBuddy.pay_my_buddy.Repository.TransactionRepository;
import com.PayMyBuddy.pay_my_buddy.Repository.UserConnectionRepository;
import com.PayMyBuddy.pay_my_buddy.Repository.UserRepository;
import com.PayMyBuddy.pay_my_buddy.Service.TransactionService;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

        @Mock
        private TransactionRepository transactionRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private UserConnectionRepository userConnectionRepository;

        @InjectMocks
        private TransactionService transactionService;

        @Test
        void createTransaction_shouldThrowException_whenSenderNotFound() {

                TransfertRequestDTO dto = new TransfertRequestDTO();
                dto.setConnectedUserId(2L);
                dto.setAmount(new BigDecimal("50.00"));

                when(userRepository.findByEmail("sender@test.com"))
                                .thenReturn(Optional.empty());

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> transactionService.createTransaction("sender@test.com", dto));

                assertEquals("Utilisateur introuvable.", exception.getMessage());
                verify(transactionRepository, never()).save(any());
        }

        @Test
        void createTransaction_shouldThrowException_whenReceiverNotFound() {

                UserEntity sender = new UserEntity();
                sender.setId(2L);

                TransfertRequestDTO dto = new TransfertRequestDTO();
                dto.setConnectedUserId(2L);
                dto.setAmount(new BigDecimal("50.00"));

                when(userRepository.findByEmail("sender@test.com"))
                                .thenReturn(Optional.of(sender));

                when(userRepository.findById(2L))
                                .thenReturn(Optional.empty());

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> transactionService.createTransaction("sender@test.com", dto));

                assertEquals("Bénéficiaire introuvable.", exception.getMessage());
                verify(transactionRepository, never()).save(any());
        }

        @Test
        void createTransaction_shouldThrowException_whenBalanceIsInsufficient() {

                UserEntity sender = new UserEntity();
                sender.setId(1L);
                sender.setBalance(new BigDecimal("20.00"));

                UserEntity receiver = new UserEntity();
                receiver.setId(2L);
                receiver.setBalance(new BigDecimal("100.00"));

                TransfertRequestDTO dto = new TransfertRequestDTO();
                dto.setConnectedUserId(2L);
                dto.setAmount(new BigDecimal("50.00"));

                when(userRepository.findByEmail("sender@test.com"))
                                .thenReturn(Optional.of(sender));

                when(userRepository.findById(2L))
                                .thenReturn(Optional.of(receiver));

                when(userConnectionRepository.existsByUser_IdAndConnectedUser_Id(1L, 2L))
                                .thenReturn(true);

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> transactionService.createTransaction("sender@test.com", dto));

                assertEquals("Solde insuffisant.", exception.getMessage());
                verify(transactionRepository, never()).save(any());

        }

        @Test
        void createTransaction_shouldDebitSenderCreditReceiverAndSaveTransaction() {

                UserEntity sender = new UserEntity();
                sender.setId(1L);
                sender.setBalance(new BigDecimal("300.00"));

                UserEntity receiver = new UserEntity();
                receiver.setId(2L);
                receiver.setBalance(new BigDecimal("100.00"));

                TransfertRequestDTO dto = new TransfertRequestDTO();
                dto.setConnectedUserId(2L);
                dto.setDescription("Restaurant");
                dto.setAmount(new BigDecimal("50.00"));

                when(userRepository.findByEmail("sender@test.com"))
                                .thenReturn(Optional.of(sender));

                when(userRepository.findById(2L))
                                .thenReturn(Optional.of(receiver));

                when(userConnectionRepository.existsByUser_IdAndConnectedUser_Id(1L, 2L))
                                .thenReturn(true);

                transactionService.createTransaction("sender@test.com", dto);

                assertEquals(new BigDecimal("250.00"), sender.getBalance());
                assertEquals(new BigDecimal("150.00"), receiver.getBalance());

                ArgumentCaptor<TransactionEntity> captor = ArgumentCaptor.forClass(TransactionEntity.class);

                verify(transactionRepository).save(captor.capture());

                TransactionEntity savedTransaction = captor.getValue();

                assertEquals(sender, savedTransaction.getSender());
                assertEquals(receiver, savedTransaction.getReceiver());
                assertEquals("Restaurant", savedTransaction.getDescription());
                assertEquals(new BigDecimal("50.00"), savedTransaction.getAmount());

        }

        @Test
        void findTransactionsByUserEmail_shouldReturnUserTransactions() {

                UserEntity user = new UserEntity();
                user.setId(1L);

                List<TransactionEntity> transactions = List.of(new TransactionEntity());

                when(userRepository.findByEmail("sender@test.com"))
                                .thenReturn(Optional.of(user));

                when(transactionRepository.findBySender_IdOrderByIdDesc(1L))
                                .thenReturn(transactions);

                List<TransactionEntity> result = transactionService.findTransactionsByUserEmail("sender@test.com");

                assertEquals(transactions, result);

        }

}
