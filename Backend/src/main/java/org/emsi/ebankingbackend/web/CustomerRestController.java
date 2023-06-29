package org.emsi.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.emsi.ebankingbackend.dtos.CustomerAccountDTO;
import org.emsi.ebankingbackend.dtos.CustomerDTO;
import org.emsi.ebankingbackend.entities.Customer;
import org.emsi.ebankingbackend.exceptions.BankAccountNotFound;
import org.emsi.ebankingbackend.exceptions.CustomerNotFoundException;
import org.emsi.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    private BankAccountService bankAccountService;
    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountService.lisCustomers();
    }
    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(@RequestParam(name="keyword" , defaultValue = "") String keyWord){
        return bankAccountService.searchCustomers(keyWord);
    }
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long costomerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(costomerId);
    }
    @GetMapping("/customer/accounts/{id}")
    public CustomerAccountDTO customerAccounts(@PathVariable(name = "id") Long customerId ,
                                               @RequestParam (name="page",defaultValue = "0") int page,
                                               @RequestParam (name="size",defaultValue = "5") int size) throws CustomerNotFoundException, BankAccountNotFound {
        return bankAccountService.customerAccount(customerId, page , size);
    }
    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }
    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCostumer(@PathVariable Long customerId ,@RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }
    @DeleteMapping("/customers/{id}")
    public void delete (@PathVariable Long id){
        bankAccountService.deleteCustomer(id);
    }
}
