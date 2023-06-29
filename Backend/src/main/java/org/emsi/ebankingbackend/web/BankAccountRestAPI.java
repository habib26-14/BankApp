package org.emsi.ebankingbackend.web;

import lombok.AllArgsConstructor;
import org.emsi.ebankingbackend.dtos.*;
import org.emsi.ebankingbackend.exceptions.BalanceNotsufficientException;
import org.emsi.ebankingbackend.exceptions.BankAccountNotFound;
import org.emsi.ebankingbackend.exceptions.CustomerNotFoundException;
import org.emsi.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFound {
        return bankAccountService.getbBankAccount(accountId);
    }
    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccount(){
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}/operation")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam (name="page",defaultValue = "0") int page,
            @RequestParam (name="size",defaultValue = "5")   int size) throws BankAccountNotFound {
        return bankAccountService.accountHistory(accountId, page , size);
    }
    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFound, BalanceNotsufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }
    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFound, BalanceNotsufficientException {
        this.bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }
    @PostMapping("/accounts/transfer")
    public TransfertRequestDTO transfer(@RequestBody TransfertRequestDTO transfertRequestDTO) throws BankAccountNotFound, BalanceNotsufficientException {
        this.bankAccountService.transfer(transfertRequestDTO.getAccountSource(), transfertRequestDTO.getAccountDestination() , transfertRequestDTO.getAmount());
        return transfertRequestDTO;
    }
    @PostMapping("/account/saveSavingAccount")
    public SaveSavingAccountDTO savingAccount(@RequestBody SaveSavingAccountDTO saveSavingAccountDTO) throws CustomerNotFoundException {
        this.bankAccountService.saveSavingBankAccount(saveSavingAccountDTO.getInitialBalance(), saveSavingAccountDTO.getInterestRate() , saveSavingAccountDTO.getCustomerId());
        return saveSavingAccountDTO;
    }
    @PostMapping("/account/saveCurrentAccount")
    public SaveCurrentAccountDTO currentAccount(@RequestBody SaveCurrentAccountDTO saveCurrentAccountDTO) throws CustomerNotFoundException {
        this.bankAccountService.saveSavingBankAccount(saveCurrentAccountDTO.getInitialBalance(), saveCurrentAccountDTO.getOverDreaft() , saveCurrentAccountDTO.getCustomerId());
        return saveCurrentAccountDTO;
    }
}
