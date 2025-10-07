using Lab5.Application.Abstractions.Repositories;
using Lab5.Application.Contracts;
using Lab5.Application.Contracts.Accounts;
using Lab5.Application.Models.Accounts;
using Lab5.Application.Models.Transactions;
using System.Collections.ObjectModel;

namespace Lab5.Application.Accounts;

public class AccountService : IAccountService
{
    private readonly IAccountRepository _accountRepository;

    public Account? Account { get; set; }

    public AccountService(IAccountRepository accountRepository)
    {
        _accountRepository = accountRepository;
        Account = null;
    }

    public LoginResult Login(long number, string password)
    {
        Account? account = _accountRepository.FindAccountByNumber(number);
        if (account?.Password == password)
        {
            Account = account;

            return new LoginResult.Success();
        }

        return new LoginResult.NotFound();
    }

    public void Refill(int amount)
    {
        if (Account != null)
        {
            Account.Balance += amount;
            var list = Account.Transactions.ToList();
            list.Add(new Transaction(TransactionType.Refill, amount));

            Account.Transactions = list;
            _accountRepository.UpdateAccount(Account);
        }
    }

    public double CheckBalance()
    {
        if (Account != null)
        {
            var list = Account.Transactions.ToList();
            list.Add(new Transaction(TransactionType.Check, 0));
            return Account.Balance;
        }

        return 0;
    }

    public void Withdraw(int amount)
    {
        if (Account != null && Account.Balance > amount)
        {
            Account.Balance -= amount;
            var list = Account.Transactions.ToList();
            list.Add(new Transaction(TransactionType.Withdraw, amount));

            Account.Transactions = list;
            _accountRepository.UpdateAccount(Account);
        }
    }

    public IEnumerable<Transaction> GetTransactionHistory()
    {
        if (Account != null)
        {
            return (ReadOnlyCollection<Transaction>)Account.Transactions;
        }

        return new ReadOnlyCollection<Transaction>(new List<Transaction>());
    }
}