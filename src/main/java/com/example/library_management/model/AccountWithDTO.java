package com.example.library_management.model;

import com.example.library_management.dto.AccountDTO;

public record AccountWithDTO(Account account, AccountDTO accountDTO) { }
