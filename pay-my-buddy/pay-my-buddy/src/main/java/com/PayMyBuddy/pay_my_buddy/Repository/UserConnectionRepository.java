package com.PayMyBuddy.pay_my_buddy.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PayMyBuddy.pay_my_buddy.Entity.UserConnectionEntity;
import com.PayMyBuddy.pay_my_buddy.Entity.UserConnectionId;

/**
 * Repository chargé de l'accès aux données des relations entre utilisateurs
 */
@Repository
public interface UserConnectionRepository extends JpaRepository<UserConnectionEntity, UserConnectionId> {

    List<UserConnectionEntity> findByUser_Id(Long userId);

    boolean existsByUser_IdAndConnectedUser_Id(Long userId, Long connectedUserId);

    void deleteByUser_IdAndConnectedUser_Id(Long userId, Long connectedUserId);

}
