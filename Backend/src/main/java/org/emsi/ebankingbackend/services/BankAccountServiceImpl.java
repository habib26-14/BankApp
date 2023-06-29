package org.emsi.ebankingbackend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.emsi.ebankingbackend.dtos.*;
import org.emsi.ebankingbackend.entities.*;
import org.emsi.ebankingbackend.enums.OperationType;
import org.emsi.ebankingbackend.exceptions.BalanceNotsufficientException;
import org.emsi.ebankingbackend.exceptions.BankAccountNotFound;
import org.emsi.ebankingbackend.exceptions.CustomerNotFoundException;
import org.emsi.ebankingbackend.mappers.BankAccountMapperImpl;
import org.emsi.ebankingbackend.repositories.AccountOperationRepository;
import org.emsi.ebankingbackend.repositories.BankAccountRepository;
import org.emsi.ebankingbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving a new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer saveCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(saveCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDreaft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        CurrentAccount currentAccount = new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreateAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDreaft);
        CurrentAccount savedBanAccount = bankAccountRepository.save(currentAccount);

        return dtoMapper.fromCurrentBankAccount(savedBanAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        System.out.println(customerId);
        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreateAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInteresRate(interestRate);
        SavingAccount savedBanAccount = bankAccountRepository.save(savingAccount);

        return dtoMapper.fromSavingBankAccount(savedBanAccount);
    }


    @Override
    public List<CustomerDTO> lisCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getbBankAccount(String accountid) throws BankAccountNotFound {
        BankAccount bankAccount = bankAccountRepository.findById(accountid).orElseThrow(() ->
                new BankAccountNotFound("Bank account not found")
        );
        if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFound, BalanceNotsufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() ->
                new BankAccountNotFound("Bank account not found")
        );
        if (bankAccount.getBalance() < amount)
            throw new BalanceNotsufficientException("Balance Not sufficient");

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFound {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() ->
                new BankAccountNotFound("Bank account not found")
        );

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFound, BalanceNotsufficientException {
        debit(accountIdSource, amount, "transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "transfer from " + accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                BankAccount savingAccount = bankAccount;
                return dtoMapper.fromSavingBankAccount((SavingAccount) savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long customerid) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerid)
                .orElseThrow(() -> new CustomerNotFoundException("User Not Found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("saving a new customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer saveCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(saveCustomer);
    }

    @Override
    public void deleteCustomer(Long costumerId) {
        customerRepository.deleteById(costumerId);

    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream()
                .map(op -> dtoMapper.fromAccountOperation(op))
                .collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO accountHistory(String accountId, int page, int size) throws BankAccountNotFound {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount == null) throw new BankAccountNotFound("Account Not found");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;

    }

    @Override
    public CustomerAccountDTO customerAccount(Long customerId, int page, int size) throws  CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer==null) throw new CustomerNotFoundException("Customer Not Found");
        Page<BankAccount> bankAccounts=bankAccountRepository.findBankAccountByCustomerOrderByCreateAt(customer ,PageRequest.of(page, size));
        CustomerAccountDTO customerAccountDTO= new CustomerAccountDTO();
        customerAccountDTO.setId(customer.getId());
        customerAccountDTO.setName(customer.getName());
        customerAccountDTO.setEmail(customer.getEmail());
        customerAccountDTO.setPageSize(size);
        customerAccountDTO.setPageSize(page);

        List<BankAccountDTO>bankAccountDTOS=bankAccounts.stream().map(bank -> {
                                    if (bank instanceof SavingAccount) {
                                        return dtoMapper.fromSavingBankAccount((SavingAccount)bank);
                                    } else {
                                        return dtoMapper.fromCurrentBankAccount((CurrentAccount) bank);
                                    }
        }).collect(Collectors.toList());
        customerAccountDTO.setTotalPages(bankAccounts.getTotalPages());
        customerAccountDTO.setBankAccountDTOS(bankAccountDTOS);

        return customerAccountDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyWord) {
        List<Customer> customers=customerRepository.searchCustomr(keyWord);
        List<CustomerDTO> collectDtos = customers.stream().map(cust -> dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
        return collectDtos;
    }
}
