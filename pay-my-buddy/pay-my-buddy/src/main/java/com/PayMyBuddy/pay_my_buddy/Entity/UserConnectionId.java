package com.PayMyBuddy.pay_my_buddy.Entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserConnectionId implements Serializable {

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "CONNECTED_USER_ID")
    private Long connectedUserId;

}
