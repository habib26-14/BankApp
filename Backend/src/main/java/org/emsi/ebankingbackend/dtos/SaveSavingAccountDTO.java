package org.emsi.ebankingbackend.dtos;

import lombok.Data;

@Data
public class SaveSavingAccountDTO {
    private double initialBalance ;
    private double interestRate;
    private Long customerId ;
}
