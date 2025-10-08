package com.project1.models.transactions;

/**
 * Класс, представляющий транзакцию.
 */
public class Transaction {
    private TransactionType type;
    private Integer amount;

    /**
     * Конструктор транзакции.
     *
     * @param type   тип транзакции
     * @param amount сумма транзакции (может быть null)
     */
    public Transaction(TransactionType type, Integer amount) {
        this.type = type;
        this.amount = amount;
    }

    /**
     * Возвращает тип транзакции.
     *
     * @return тип транзакции
     */
    public TransactionType getType() {
        return type;
    }

    /**
     * Устанавливает тип транзакции.
     *
     * @param type тип транзакции
     */
    public void setType(TransactionType type) {
        this.type = type;
    }

    /**
     * Возвращает сумму транзакции.
     *
     * @return сумма транзакции
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Устанавливает сумму транзакции.
     *
     * @param amount сумма транзакции
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
