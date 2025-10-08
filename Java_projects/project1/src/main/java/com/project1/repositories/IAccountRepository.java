package com.project1.repositories;

import com.project1.models.Account;

/**
 * Интерфейс для операций с аккаунтами.
 */
public interface IAccountRepository {
    /**
     * Ищет аккаунт по его номеру.
     *
     * @param number номер аккаунта
     * @return найденный аккаунт или null, если аккаунт не найден
     */
    Account findAccountByNumber(long number);

    /**
     * Создаёт новый аккаунт.
     *
     * @param number   номер аккаунта
     * @param password пароль аккаунта
     */
    void createAccount(int number, String password);

    /**
     * Обновляет данные аккаунта.
     *
     * @param account аккаунт для обновления
     */
    void updateAccount(Account account);
}
