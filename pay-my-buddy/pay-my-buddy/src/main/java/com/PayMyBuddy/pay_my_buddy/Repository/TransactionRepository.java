package com.PayMyBuddy.pay_my_buddy.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PayMyBuddy.pay_my_buddy.Entity.TransactionEntity;

/**
 * Repository chargé de l'accès aux données des transactions
 */
@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findBySender_IdOrderByIdDesc(Long senderId);
}
