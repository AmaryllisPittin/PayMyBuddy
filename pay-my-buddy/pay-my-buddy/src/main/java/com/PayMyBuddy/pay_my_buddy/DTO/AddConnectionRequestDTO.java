package com.PayMyBuddy.pay_my_buddy.DTO;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * DTO pour créer une nouvelle relation entre utilisateurs
 */
@Getter
@Setter
public class AddConnectionRequestDTO {
    private Long connectedUserId;
    private String username;
}
