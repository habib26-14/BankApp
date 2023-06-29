package org.emsi.ebankingbackend.services;

import org.emsi.ebankingbackend.dtos.*;
import org.emsi.ebankingbackend.entities.BankAccount;
import org.emsi.ebankingbackend.entities.CurrentAccount;
import org.emsi.ebankingbackend.entities.Customer;
import org.emsi.ebankingbackend.entities.SavingAccount;
import org.emsi.ebankingbackend.exceptions.BalanceNotsufficientException;
import org.emsi.ebankingbackend.exceptions.BankAccountNotFound;
import org.emsi.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {



    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance , double overDreaft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance , double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> lisCustomers();
    BankAccountDTO getbBankAccount(String accountid) throws BankAccountNotFound;
    void debit(String accountId , double amount , String description) throws BankAccountNotFound, BalanceNotsufficientException;
    void credit(String accountId , double amount , String description) throws BalanceNotsufficientException, BankAccountNotFound;
    void transfer(String accountIdSource ,String accountIdDestination , double amount ) throws BankAccountNotFound, BalanceNotsufficientException;
    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(Long customerid) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long costumerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO accountHistory(String accountId , int page , int size) throws BankAccountNotFound;
    CustomerAccountDTO customerAccount ( Long customerId , int page , int size) throws BankAccountNotFound, CustomerNotFoundException;



    List<CustomerDTO> searchCustomers(String keyWord);
}
