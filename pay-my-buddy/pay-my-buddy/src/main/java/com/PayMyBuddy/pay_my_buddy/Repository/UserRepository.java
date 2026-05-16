package com.PayMyBuddy.pay_my_buddy.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PayMyBuddy.pay_my_buddy.Entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    // Reconnaissance du user par mail pour le login
    Optional<UserEntity> findByEmail(String email);

    // Eviter les doublons dans les inscriptions
    boolean existsByEmail(String email);

    // Eviter les doublons dans les username
    boolean existsByUsername(String username);

}
