package com.project1.models;

import com.project1.models.transactions.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий банковский аккаунт.
 */
public class Account {
    private int number;
    private String password;
    private int balance;
    private List<Transaction> transactions;

    /**
     * Конструктор аккаунта.
     *
     * @param number   номер аккаунта
     * @param password пароль аккаунта
     */
    public Account(int number, String password) {
        this.number = number;
        this.password = password;
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }

    /**
     * Возвращает номер аккаунта.
     *
     * @return номер аккаунта
     */
    public int getNumber() {
        return number;
    }

    /**
     * Устанавливает номер аккаунта.
     *
     * @param number номер аккаунта
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Возвращает пароль аккаунта.
     *
     * @return пароль аккаунта
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль аккаунта.
     *
     * @param password пароль аккаунта
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Возвращает баланс аккаунта.
     *
     * @return баланс аккаунта
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Устанавливает баланс аккаунта.
     *
     * @param balance баланс аккаунта
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Возвращает список транзакций аккаунта.
     *
     * @return список транзакций
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Устанавливает список транзакций аккаунта.
     *
     * @param transactions список транзакций
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}