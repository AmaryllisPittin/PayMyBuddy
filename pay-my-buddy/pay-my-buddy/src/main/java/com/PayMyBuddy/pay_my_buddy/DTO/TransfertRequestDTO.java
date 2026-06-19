package com.PayMyBuddy.pay_my_buddy.DTO;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransfertRequestDTO {
    private Long connectedUserId;
    private String description;
    private BigDecimal amount;
}
