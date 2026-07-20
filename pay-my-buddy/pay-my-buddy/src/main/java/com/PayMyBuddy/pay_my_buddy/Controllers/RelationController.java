package com.PayMyBuddy.pay_my_buddy.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.PayMyBuddy.pay_my_buddy.DTO.AddRelationRequestDTO;
import com.PayMyBuddy.pay_my_buddy.Service.UserConnectionService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * Controller chargé de la gestion des relations entre utilisateurs
 */
@Controller
@RequiredArgsConstructor
public class RelationController {

    private final UserConnectionService userConnectionService;

    @GetMapping("/relation")
    public String relationPage() {
        return "relation";
    }

    // Ajout d'une nouvelle relation à partir de l'adresse mail de l'utilisateur
    @PostMapping("/relation")
    public String addRelation(@ModelAttribute AddRelationRequestDTO dto,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        userConnectionService.addConnectionByEmail(authentication.getName(), dto.getEmail());
        redirectAttributes.addFlashAttribute("success", "Relation ajoutée.");
        return "redirect:/relation";

    }

}
