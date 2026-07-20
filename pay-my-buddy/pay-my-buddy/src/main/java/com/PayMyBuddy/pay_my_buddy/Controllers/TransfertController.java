package com.PayMyBuddy.pay_my_buddy.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.PayMyBuddy.pay_my_buddy.DTO.TransfertRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Service.TransactionService;
import com.PayMyBuddy.pay_my_buddy.Service.UserConnectionService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * Controller chargé de la gestion des transferts d'argent entre utilisateurs
 */
@Controller
@RequiredArgsConstructor
public class TransfertController {

    private final UserConnectionService userConnectionService;
    private final TransactionService transactionService;

    // Affichage de la page de transfert : relations + historique des transactions
    @GetMapping("/transfert")
    public String transfertPage(Model model, Authentication authentication) {

        String email = authentication.getName();

        model.addAttribute("relations", userConnectionService.getConnections(email));
        model.addAttribute("transactions", transactionService.findTransactionsByUserEmail(email));

        return "transfert";
    }

    // Effectue le transfert d'argent et affiche un message de succès ou d'erreur
    // après redirection
    @PostMapping("/transfert")
    public String makeTransfert(@ModelAttribute TransfertRequestDTO dto, Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String email = authentication.getName();

        try {
            transactionService.createTransaction(email, dto);
            redirectAttributes.addFlashAttribute("success", "le virement a été effectué.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/transfert";
    }

}
