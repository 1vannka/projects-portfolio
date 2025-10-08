package com.project1.services;

import com.project1.exceptions.NotEnoughMoneyException;
import com.project1.exceptions.NotFoundAccountException;
import com.project1.models.Account;
import com.project1.models.transactions.Transaction;
import com.project1.models.transactions.TransactionType;
import com.project1.repositories.IAccountRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация сервиса для работы с аккаунтами.
 */
public class AccountService  {
    private final IAccountRepository accountRepository;
    private Account account;

    /**
     * Конструктор сервиса.
     *
     * @param accountRepository репозиторий аккаунтов
     */
    public AccountService(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.account = null;
    }

    /**
     * Выполняет вход в аккаунт.
     *
     * @param number   номер аккаунта
     * @param password пароль аккаунта
     * @return найденный аккаунт
     */
    public Account Login(long number, String password) throws NotFoundAccountException {
        Account foundAccount = accountRepository.findAccountByNumber(number);
        if (foundAccount != null && foundAccount.getPassword().equals(password)) {
            this.account = foundAccount;
            return foundAccount;
        }
        throw new NotFoundAccountException("Неверный номер счета или пароль.");
    }

    /**
     * Пополняет баланс авторизованного аккаунта.
     *
     * @param amount сумма пополнения
     */
    public void refill(int amount) throws NotFoundAccountException {
        if (account == null) {
            throw new NotFoundAccountException("Аккаунт не авторизован.");
        }
        account.setBalance(account.getBalance() + amount);
        List<Transaction> transactions = new ArrayList<>(account.getTransactions());
        transactions.add(new Transaction(TransactionType.REFILL, amount));
        account.setTransactions(transactions);
        accountRepository.updateAccount(account);
    }

    /**
     * Возвращает баланс авторизованного аккаунта.
     *
     * @return текущий баланс
     */
    public double checkBalance() throws NotFoundAccountException {
        if (account == null) {
            throw new NotFoundAccountException("Аккаунт не авторизован.");
        }
        List<Transaction> transactions = new ArrayList<>(account.getTransactions());
        transactions.add(new Transaction(TransactionType.CHECK, 0));
        return account.getBalance();
    }

    /**
     * Снимает указанную сумму с баланса авторизованного аккаунта.
     *
     * @param amount сумма для снятия
     */
    public void withdraw(int amount) throws NotFoundAccountException, NotEnoughMoneyException {
        if (account == null) {
            throw new NotFoundAccountException("Аккаунт не авторизован.");
        }
        if (account.getBalance() <= amount) {
            throw new NotEnoughMoneyException("Недостаточно средств.");
        }
        account.setBalance(account.getBalance() - amount);
        List<Transaction> transactions = new ArrayList<>(account.getTransactions());
        transactions.add(new Transaction(TransactionType.WITHDRAW, amount));
        account.setTransactions(transactions);
        accountRepository.updateAccount(account);
    }

    /**
     * Возвращает историю транзакций авторизованного аккаунта.
     *
     * @return список транзакций
     */
    public List<Transaction> getTransactionHistory() throws NotFoundAccountException {
        if (account == null) {
            throw new NotFoundAccountException("Аккаунт не авторизован.");
        }
        return new ArrayList<>(account.getTransactions());
    }
}
