package com.ingenico.transferservice.persistence.repository;

import com.ingenico.transferservice.persistence.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
