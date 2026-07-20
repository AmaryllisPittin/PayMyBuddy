package com.PayMyBuddy.pay_my_buddy.DTO;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * DTO pour la demande de connexion d'un utilisateur
 */
@Getter
@Setter
public class LoginRequestDTO {

    private String email;
    private String password;

}
