package com.PayMyBuddy.pay_my_buddy.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * Entité représentant une relation entre deux utilisateurs
 */
@Entity
@Table(name = "user_connections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserConnectionEntity {

    @EmbeddedId
    private UserConnectionId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @MapsId("connectedUserId")
    @ManyToOne
    @JoinColumn(name = "CONNECTED_USER_ID")
    private UserEntity connectedUser;

}
