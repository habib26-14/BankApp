package org.emsi.ebankingbackend.dtos;

import lombok.Data;

@Data
public class SaveCurrentAccountDTO {
    private double initialBalance ;
    private double overDreaft;
    private Long customerId ;
}
