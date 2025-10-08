using Lab5.Application.Models.Transactions;

namespace Lab5.Application.Models.Accounts;

public class Account
{
    public Account(int number, string password)
    {
        Number = number;
        Password = password;
        Balance = 0;
        Transactions = new List<Transaction>();
    }

    public int Number { get; set; }

    public string Password { get; set; }

    public int Balance { get; set; }

    public IEnumerable<Transaction> Transactions { get; set; }
}