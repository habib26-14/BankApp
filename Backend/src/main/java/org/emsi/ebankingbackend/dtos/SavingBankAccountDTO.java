package org.emsi.ebankingbackend.dtos;

import jakarta.persistence.*;
import lombok.Data;
import org.emsi.ebankingbackend.enums.AccountStatus;

import java.util.Date;

@Data


public class SavingBankAccountDTO extends BankAccountDTO{

    private String id ;
    private double balance ;
    private Date createAt;
    private AccountStatus status;
    private CustomerDTO customerDTO ;
    private double interesRate;

}
