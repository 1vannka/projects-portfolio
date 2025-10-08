using Lab5.Application.Models.Transactions;

namespace Lab5.Application.Abstractions.Repositories;

public interface ITransactionRepository
{
    public IEnumerable<Transaction> GetAll();
}