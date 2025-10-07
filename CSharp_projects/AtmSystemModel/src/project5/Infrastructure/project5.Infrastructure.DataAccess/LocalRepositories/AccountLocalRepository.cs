using Lab5.Application.Abstractions.Repositories;
using Lab5.Application.Models.Accounts;

namespace Lab5.Infrastructure.DataAccess.LocalRepositories;

public class AccountLocalRepository : IAccountRepository
{
    private IEnumerable<Account> _accounts;

    public AccountLocalRepository()
    {
        _accounts = new List<Account>();
    }

    public Account? FindAccountByNumber(long number)
    {
        foreach (Account account in _accounts)
        {
            if (account.Number == number)
            {
                return account;
            }
        }

        return null;
    }

    public void CreateAccount(int number, string password)
    {
        if (FindAccountByNumber(number) is null)
        {
            var list = _accounts.ToList();
            list.Add(new Account(number, password));

            _accounts = list;
        }
    }

    public void UpdateAccount(Account account)
    {
        Account? accountByNumber = FindAccountByNumber(account.Number);

        if (accountByNumber != null)
        {
            accountByNumber.Balance = account.Balance;
        }
    }
}