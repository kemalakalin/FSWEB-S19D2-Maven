package com.workintech.s18d4.dto;

import com.workintech.s18d4.entity.Account;

public record AccountResponse(Long id, String accountName, Double moneyAmount, CustomerResponse customerResponse) {

}

