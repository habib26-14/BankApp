package org.emsi.ebankingbackend.services;

import jakarta.transaction.Transactional;
import org.emsi.ebankingbackend.entities.BankAccount;
import org.emsi.ebankingbackend.entities.CurrentAccount;
import org.emsi.ebankingbackend.entities.SavingAccount;
import org.emsi.ebankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount = bankAccountRepository.findById("103399cd-f8ba-46e4-8d60-109aaee3f5fd").orElse(null);
        if(bankAccount!=null) {
            System.out.println("=================================");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreateAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Over Draft =>" + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Rate =>" + ((SavingAccount) bankAccount).getInteresRate());
            }

            System.out.println("=================================");
            bankAccount.getAccountOperations().forEach(op->{
                System.out.println("*****************op*****************");
                System.out.println(op.getType());
                System.out.println(op.getAmount());
                System.out.println(op.getOperationDate());
            });
        }
    }
}
