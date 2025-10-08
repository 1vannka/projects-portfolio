using Lab5.Application.Models.Accounts;

namespace Lab5.Application.Abstractions.Repositories;

public interface IAccountRepository
{
    public Account? FindAccountByNumber(long number);

    public void CreateAccount(int number, string password);

    public void UpdateAccount(Account account);
}