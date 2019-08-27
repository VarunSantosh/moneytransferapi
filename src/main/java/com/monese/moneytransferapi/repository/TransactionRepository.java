package com.monese.moneytransferapi.repository;

import com.monese.moneytransferapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
