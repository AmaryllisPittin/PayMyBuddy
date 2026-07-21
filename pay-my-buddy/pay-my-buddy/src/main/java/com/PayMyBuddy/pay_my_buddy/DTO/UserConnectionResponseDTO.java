package com.PayMyBuddy.pay_my_buddy.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * DTO contenant les informations d'une relation utilisateur
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserConnectionResponseDTO {
    private Long connectedUserId;
    private String username;
    private String email;
}
