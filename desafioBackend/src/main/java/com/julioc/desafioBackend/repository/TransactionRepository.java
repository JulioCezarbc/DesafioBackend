package com.julioc.desafioBackend.repository;

import com.julioc.desafioBackend.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
}
