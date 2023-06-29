package org.emsi.ebankingbackend.repositories;

import org.emsi.ebankingbackend.entities.BankAccount;
import org.emsi.ebankingbackend.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
   Page<BankAccount> findBankAccountByCustomerOrderByCreateAt(Customer customer , Pageable pageable);
}
