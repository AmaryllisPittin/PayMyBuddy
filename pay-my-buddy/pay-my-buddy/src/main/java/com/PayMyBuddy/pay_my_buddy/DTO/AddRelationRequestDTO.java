package com.PayMyBuddy.pay_my_buddy.DTO;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * DTO contenant le mail de l'utilisateur pour l'ajouter en tant que relation
 */
@Getter
@Setter
public class AddRelationRequestDTO {
    private String email;
}
