package com.PayMyBuddy.pay_my_buddy.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.PayMyBuddy.pay_my_buddy.DTO.TransfertRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Service.TransactionService;
import com.PayMyBuddy.pay_my_buddy.Service.UserConnectionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TransfertController {

    private final UserConnectionService userConnectionService;
    private final TransactionService transactionService;

    @GetMapping("/transfert")
    public String transfertPage(Model model, Authentication authentication) {

        String email = authentication.getName();

        model.addAttribute("relations", userConnectionService.getConnections(email));
        model.addAttribute("transactions", transactionService.findTransactionsByUserEmail(email));

        return "transfert";
    }

    @PostMapping("/transfert")
    public String makeTransfert(@ModelAttribute TransfertRequestDTO dto, Authentication authentication) {
        String email = authentication.getName();
        transactionService.createTransaction(email, dto);
        return "redirect:/transfert";
    }

}
