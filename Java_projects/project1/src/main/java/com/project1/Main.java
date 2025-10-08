package com.project1;

import com.project1.models.Account;
import com.project1.models.transactions.Transaction;
import com.project1.repositories.AccountRepository;
import com.project1.services.AccountService;

import java.util.Scanner;

/**
 * Главный класс для работы консольного приложения банкомата.
 */
public class Main {
    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccountRepository accountRepository = new AccountRepository();
        AccountService accountService = new AccountService(accountRepository);

        System.out.println("Добро пожаловать в банкомат!");

        while (true) {
            System.out.println("\n1. Вход в аккаунт");
            System.out.println("2. Создать аккаунт");
            System.out.println("3. Выход");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // считываем символ перевода строки


            if (choice == 1) {
                System.out.print("Введите номер счета: ");
                long accountNumber = scanner.nextLong();
                scanner.nextLine();
                System.out.print("Введите пароль: ");
                String password = scanner.nextLine();

                try {
                    Account account = accountService.Login(accountNumber, password);
                    System.out.println("Успешный вход в аккаунт!");
                    accountMenu(scanner, accountService);
                } catch (Exception e) {
                    System.out.println("Ошибка входа: " + e.getMessage());
                }
            }

            if (choice == 2) {
                System.out.println("Введите номер нового аккаунта");
                int accountNumber = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Введите новый пароль");
                String password = scanner.nextLine();

                try {
                    accountRepository.createAccount(accountNumber, password);
                } catch (Exception e) {
                    System.out.println("Ошибка входа: " + e.getMessage());
                }

            }

            if (choice == 3) {
                System.out.println("Спасибо за использование банкомата. До свидания!");
                break;
            }


        }
    }

    /**
     * Отображает меню для операций с аккаунтом.
     *
     * @param scanner        объект Scanner для ввода данных
     * @param accountService сервис для работы с аккаунтом
     */
    private static void accountMenu(Scanner scanner, AccountService accountService) {
        while (true) {
            System.out.println("\n1. Проверить баланс");
            System.out.println("2. Пополнить счет");
            System.out.println("3. Снять деньги");
            System.out.println("4. История транзакций");
            System.out.println("5. Выйти из аккаунта");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // считываем символ перевода строки

            try {
                switch (choice) {
                    case 1 -> {
                        double balance = accountService.checkBalance();
                        System.out.println("Ваш баланс: " + balance);
                    }
                    case 2 -> {
                        System.out.print("Введите сумму пополнения: ");
                        int amount = scanner.nextInt();
                        scanner.nextLine();
                        accountService.refill(amount);
                        System.out.println("Счет пополнен на " + amount);
                    }
                    case 3 -> {
                        System.out.print("Введите сумму для снятия: ");
                        int amount = scanner.nextInt();
                        scanner.nextLine();
                        accountService.withdraw(amount);
                        System.out.println("Снято: " + amount);
                    }
                    case 4 -> {
                        System.out.println("История транзакций:");
                        for (Transaction transaction : accountService.getTransactionHistory()) {
                            System.out.println(transaction.getType() + " — " + transaction.getAmount());
                        }
                    }
                    case 5 -> {
                        System.out.println("Выход из аккаунта.");
                        return;
                    }
                    default -> System.out.println("Некорректный выбор. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}
