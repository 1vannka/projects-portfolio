package com.project1.repositories;

import com.project1.models.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория аккаунтов с использованием локального хранилища.
 * Этот класс предоставляет методы для поиска, создания и обновления аккаунтов.
 */
public class AccountRepository implements IAccountRepository {
    private List<Account> accounts;

    /**
     * Конструктор, инициализирующий пустой список аккаунтов.
     */
    public AccountRepository() {
        accounts = new ArrayList<>();
    }

    /**
     * Находит аккаунт по номеру.
     *
     * @param number номер аккаунта
     * @return найденный аккаунт или null, если аккаунт с таким номером не найден
     */
    @Override
    public Account findAccountByNumber(long number) {
        for (Account account : accounts) {
            if (account.getNumber() == number) {
                return account;
            }
        }
        return null;
    }

    /**
     * Создает новый аккаунт, если аккаунт с указанным номером еще не существует.
     *
     * @param number  номер нового аккаунта
     * @param password пароль для нового аккаунта
     */
    @Override
    public void createAccount(int number, String password) {
        if (findAccountByNumber(number) == null) {
            accounts.add(new Account(number, password));
        }
    }

    /**
     * Обновляет информацию о существующем аккаунте.
     * Если аккаунт с таким номером найден, обновляется его баланс.
     *
     * @param account объект аккаунта с обновленной информацией
     */
    @Override
    public void updateAccount(Account account) {
        Account accountByNumber = findAccountByNumber(account.getNumber());
        if (accountByNumber != null) {
            accountByNumber.setBalance(account.getBalance());
        }
    }
}
