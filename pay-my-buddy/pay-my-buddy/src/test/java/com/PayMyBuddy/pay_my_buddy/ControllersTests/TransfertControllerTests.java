package com.PayMyBuddy.pay_my_buddy.ControllersTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.PayMyBuddy.pay_my_buddy.Controllers.TransfertController;
import com.PayMyBuddy.pay_my_buddy.DTO.TransfertRequestDTO;
import com.PayMyBuddy.pay_my_buddy.DTO.UserConnectionResponseDTO;
import com.PayMyBuddy.pay_my_buddy.Entity.TransactionEntity;
import com.PayMyBuddy.pay_my_buddy.Service.TransactionService;
import com.PayMyBuddy.pay_my_buddy.Service.UserConnectionService;

@ExtendWith(MockitoExtension.class)
public class TransfertControllerTests {

    @Mock
    private UserConnectionService userConnectionService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private TransfertController transfertController;

    @Test
    void transfertPage_shouldAddRelationsAndTransactionsAndReturnView() {

        String email = "sender@test.com";

        List<UserConnectionResponseDTO> relations = List.of(
                mock(UserConnectionResponseDTO.class));

        List<TransactionEntity> transactions = List.of(
                new TransactionEntity());

        when(authentication.getName()).thenReturn(email);
        when(userConnectionService.getConnections(email)).thenReturn(relations);
        when(transactionService.findTransactionsByUserEmail(email)).thenReturn(transactions);

        String result = transfertController.transfertPage(model, authentication);

        assertEquals("transfert", result);

        verify(model).addAttribute("relations", relations);
        verify(model).addAttribute("transactions", transactions);

        verify(userConnectionService).getConnections(email);
        verify(transactionService).findTransactionsByUserEmail(email);

    }

    @Test
    void makeTransfert_shouldAddSuccessMessage_whenTransactionSucceeds() {

        String email = "sender@test.com";

        TransfertRequestDTO dto = new TransfertRequestDTO();
        dto.setConnectedUserId(1L);
        dto.setDescription("Restaurant");
        dto.setAmount(new BigDecimal("50.00"));

        when(authentication.getName()).thenReturn(email);

        String result = transfertController.makeTransfert(dto, authentication, redirectAttributes);

        assertEquals("redirect:/transfert", result);

        verify(transactionService).createTransaction(email, dto);
        verify(redirectAttributes).addFlashAttribute(
                "success",
                "le virement a été effectué.");

        verify(redirectAttributes, never())
                .addFlashAttribute(eq("error"), any());

    }

    @Test
    void makeTransfert_shouldAddErrorMessage_whenTransactionFails() {

        String email = "sender@test.com";

        TransfertRequestDTO dto = new TransfertRequestDTO();
        dto.setConnectedUserId(1L);
        dto.setAmount(new BigDecimal("50.00"));

        when(authentication.getName()).thenReturn(email);

        doThrow(new RuntimeException("Solde insuffisant."))
                .when(transactionService)
                .createTransaction(email, dto);

        String result = transfertController.makeTransfert(dto, authentication, redirectAttributes);

        assertEquals("redirect:/transfert", result);

        verify(redirectAttributes).addFlashAttribute(
                "error",
                "Solde insuffisant.");

        verify(redirectAttributes, never())
                .addFlashAttribute(eq("success"), any());

    }

}
