package org.emsi.ebankingbackend.dtos;

import jakarta.persistence.DiscriminatorColumn;
import lombok.Data;
import org.emsi.ebankingbackend.enums.AccountStatus;

import java.util.Date;

@Data


public class CurrentBankAccountDTO extends BankAccountDTO {

    private String id ;
    private double balance ;
    private Date createAt;
    private AccountStatus status;
    private CustomerDTO customerDTO ;
    private double overDraft ;
    private double interesRate;

}
