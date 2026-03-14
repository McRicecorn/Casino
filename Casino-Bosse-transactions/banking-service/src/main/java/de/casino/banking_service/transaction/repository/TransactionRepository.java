package de.casino.banking_service.transaction.repository;

import de.casino.banking_service.transaction.model.TransactionEntity;
import de.casino.banking_service.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findByUser(UserEntity user);
    List<TransactionEntity> findByUserId(long user_id);
}
