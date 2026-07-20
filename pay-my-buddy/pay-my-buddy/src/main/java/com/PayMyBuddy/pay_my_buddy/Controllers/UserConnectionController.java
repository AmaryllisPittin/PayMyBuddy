package com.PayMyBuddy.pay_my_buddy.Controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PayMyBuddy.pay_my_buddy.DTO.AddConnectionRequestDTO;
import com.PayMyBuddy.pay_my_buddy.DTO.UserConnectionResponseDTO;
import com.PayMyBuddy.pay_my_buddy.Service.UserConnectionService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * API REST de gestion des relations entre utilisateurs
 */
@Controller
@RequestMapping("/connections")
@RequiredArgsConstructor
public class UserConnectionController {

    private final UserConnectionService service;

    @PostMapping
    public void addConnection(@RequestBody AddConnectionRequestDTO dto, Authentication authentication) {
        service.addConnection(authentication.getName(), dto);
    }

    @GetMapping
    public List<UserConnectionResponseDTO> getConnections(Authentication authentication) {
        return service.getConnections(authentication.getName());
    }

    @DeleteMapping("/{connectedUserId}")
    public void removeConnection(
            @PathVariable Long connectedUserId,
            Authentication authentication) {
        service.removeConnection(authentication.getName(), connectedUserId);
    }
}
