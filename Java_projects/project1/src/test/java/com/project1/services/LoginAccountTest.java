package com.project1.services;

import com.project1.exceptions.NotFoundAccountException;
import com.project1.repositories.AccountRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginAccountTest {

    @Test
    void Login_ThrowsException() throws NotFoundAccountException {
        AccountRepository accountRepository = new AccountRepository();
        AccountService accountService = new AccountService(accountRepository);

        accountRepository.createAccount(12345, "password");
        assertThrows(NotFoundAccountException.class, () -> accountService.Login(12347,"password"));
    }

}
