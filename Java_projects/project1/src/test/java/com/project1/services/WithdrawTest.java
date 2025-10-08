package com.project1.services;

import com.project1.exceptions.NotEnoughMoneyException;
import com.project1.exceptions.NotFoundAccountException;
import com.project1.repositories.AccountRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WithdrawTest {
    @Test
    void Withdraw_ThrowsException() throws NotEnoughMoneyException, NotFoundAccountException {
        AccountRepository accountRepository = new AccountRepository();
        AccountService accountService = new AccountService(accountRepository);
        accountRepository.createAccount(12345, "password");
        accountService.Login(12345,"password");
        accountService.refill(1000);
        assertThrows(NotEnoughMoneyException.class, () -> accountService.withdraw(1500));
    }
}
