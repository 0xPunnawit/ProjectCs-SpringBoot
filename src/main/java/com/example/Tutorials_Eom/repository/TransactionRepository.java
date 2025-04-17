package com.example.Tutorials_Eom.repository;

import com.example.Tutorials_Eom.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Integer> {

    boolean existsByTransactionReference(String transactionReference);
}
