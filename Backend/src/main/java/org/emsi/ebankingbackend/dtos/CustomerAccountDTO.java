package org.emsi.ebankingbackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CustomerAccountDTO {
    private Long id;
    private String name;
    private String email;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    List<BankAccountDTO> bankAccountDTOS;
}
