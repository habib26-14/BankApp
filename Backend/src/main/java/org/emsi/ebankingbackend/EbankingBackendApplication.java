package org.emsi.ebankingbackend;

import org.emsi.ebankingbackend.dtos.BankAccountDTO;
import org.emsi.ebankingbackend.dtos.CurrentBankAccountDTO;
import org.emsi.ebankingbackend.dtos.CustomerDTO;
import org.emsi.ebankingbackend.dtos.SavingBankAccountDTO;
import org.emsi.ebankingbackend.entities.*;
import org.emsi.ebankingbackend.enums.AccountStatus;
import org.emsi.ebankingbackend.enums.OperationType;
import org.emsi.ebankingbackend.exceptions.BalanceNotsufficientException;
import org.emsi.ebankingbackend.exceptions.BankAccountNotFound;
import org.emsi.ebankingbackend.exceptions.CustomerNotFoundException;
import org.emsi.ebankingbackend.repositories.AccountOperationRepository;
import org.emsi.ebankingbackend.repositories.BankAccountRepository;
import org.emsi.ebankingbackend.repositories.CustomerRepository;
import org.emsi.ebankingbackend.services.BankAccountService;
import org.emsi.ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }
   @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Kali","Nathalie","Franlyn").forEach(name->{
                    CustomerDTO customer= new CustomerDTO();
                    customer.setName(name);
                    customer.setEmail(name+"@gmail.com");
                    bankAccountService.saveCustomer(customer);
            });
            bankAccountService.lisCustomers().forEach(customer->{
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*9000,9000,customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*12000,7.5,customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
            List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
            for (BankAccountDTO  bankAccount: bankAccounts) {
                for (int i = 0; i < 10; i++) {
                    String accoutId;
                    if(bankAccount instanceof SavingBankAccountDTO){
                        accoutId=((SavingBankAccountDTO) bankAccount).getId();
                    }else {
                        accoutId=((CurrentBankAccountDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accoutId , 10000+Math.random()*12000, "Credit");

                    bankAccountService.debit(accoutId , 1000+Math.random()*9000, "Debit");

                }
            }

        };
    }
    //@Bean
    CommandLineRunner start(
            CustomerRepository customerRepository ,
            BankAccountRepository bankAccountRepository ,
            AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Hassan","Said","khadija").forEach( name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);

            });
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCreateAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCreateAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInteresRate(5.5);
                bankAccountRepository.save(savingAccount);

            });

            bankAccountRepository.findAll().forEach(acc->{
                for (int i=0 ; i<5 ; i++){
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }
            });

        };
    }

}
