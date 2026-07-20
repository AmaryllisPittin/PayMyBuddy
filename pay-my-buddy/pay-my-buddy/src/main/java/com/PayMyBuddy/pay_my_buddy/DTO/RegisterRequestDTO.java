package com.PayMyBuddy.pay_my_buddy.DTO;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * DTO pour la création de compte
 */
@Getter
@Setter
public class RegisterRequestDTO {

    public String username;
    public String email;
    public String password;

}
