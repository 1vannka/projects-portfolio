package com.project1.services;

import com.project1.exceptions.NotFoundAccountException;
import com.project1.repositories.AccountRepository;
import com.project1.repositories.IAccountRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CheckAccountBalanceTest {

    @Test
    void AccountCheckBalance() throws NotFoundAccountException {
        AccountRepository accountRepository = new AccountRepository();
        AccountService accountService = new AccountService(accountRepository);
        accountRepository.createAccount(12345, "password");
        accountService.Login(12345,"password");
        accountService.refill(1000);
        assertEquals(1000,accountService.checkBalance());
    }
}
