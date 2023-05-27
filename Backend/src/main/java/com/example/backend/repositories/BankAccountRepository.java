package com.example.backend.repositories;

import com.example.backend.entities.BankAccount;
import com.example.backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
