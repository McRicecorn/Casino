package de.casino.banking_service.transaction.repository;

import de.casino.banking_service.transaction.model.TransactionEntity;
import de.casino.banking_service.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findByUser(UserEntity user);
}
