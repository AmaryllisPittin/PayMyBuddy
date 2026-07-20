package com.PayMyBuddy.pay_my_buddy.DTO;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * DTO pour le détail des transferts d'argent
 */
@Getter
@Setter
public class TransfertRequestDTO {
    private Long connectedUserId;
    private String description;
    private BigDecimal amount;
}
