namespace Lab5.Application.Models.Transactions;

public class Transaction
{
    public Transaction(TransactionType type, int? amount)
    {
        Type = type;
        Amount = amount;
    }

    public TransactionType Type { get; set; }

    public int? Amount { get; set; }
}