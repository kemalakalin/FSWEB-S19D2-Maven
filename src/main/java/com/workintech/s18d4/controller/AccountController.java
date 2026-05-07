package com.workintech.s18d4.controller;


import com.workintech.s18d4.dto.AccountResponse;
import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Account;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.AccountService;
import com.workintech.s18d4.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {
    private AccountService accountService;
    private CustomerService customerService;

    @GetMapping
    public List<Account> findAll() {
        return accountService.findAll();
    }

    @PostMapping("/{customerId}")
    public AccountResponse save(@PathVariable("customerId") long customerId, @RequestBody Account account) {
        Customer customer = customerService.find(customerId);
        if (customer != null) {
            customer.getAccounts().add(account);
            account.setCustomer(customer);
            accountService.save(account);
        } else {
            throw new RuntimeException("Customer not found");
        }
        return new AccountResponse(account.getId(), account.getAccountName(), account.getMoneyAmount(),
                new CustomerResponse(customer.getId(), customer.getEmail(), customer.getSalary()));
    }

    @PutMapping("/{customerId}")
    public AccountResponse update(@RequestBody Account account, @PathVariable long customerId) {
        Customer customer = customerService.find(customerId);

        if (customer == null) {
            throw new RuntimeException("Customer not found with id: " + customerId);
        }

        // Hesabın gerçekten bu müşteriye ait olup olmadığını kontrol ediyoruz
        // Stream kullanarak daha temiz bir kontrol yapabiliriz
        Account foundAccount = customer.getAccounts().stream()
                .filter(a -> a.getId().equals(account.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Account not found for this customer"));

        // Güncellenecek verileri set ediyoruz
        account.setCustomer(customer);

        // save metodu JPA'da hem save hem update (merge) görevi görür
        Account updatedAccount = accountService.save(account);

        return new AccountResponse(
                updatedAccount.getId(),
                updatedAccount.getAccountName(),
                updatedAccount.getMoneyAmount(),
                new CustomerResponse(customer.getId(), customer.getEmail(), customer.getSalary())
        );
    }

    @DeleteMapping("/{id}")
    public AccountResponse delete(@PathVariable long id) {
        Account account = accountService.find(id);
        if (account == null) {
            throw new RuntimeException("Account not found with id: " + id);
        }
        accountService.delete(id);

        return new AccountResponse(account.getId(), account.getAccountName(), account.getMoneyAmount(),
                new CustomerResponse(account.getCustomer().getId(), account.getCustomer().getEmail(), account.getCustomer().getSalary()));

    }
}


