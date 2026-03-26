package de.casino.banking_service.transaction.repository;

import de.casino.banking_service.transaction.model.ITransactionEntity;
import de.casino.banking_service.transaction.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ITransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findByUser_Id(long user_id);
    List<ITransactionEntity> findAllByUserId(long userId);
}
