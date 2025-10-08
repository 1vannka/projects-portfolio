using Lab5.Application.Accounts;
using Lab5.Application.Models.Transactions;
using Spectre.Console;

namespace Lab5.Presentation.Console.Scenarios;

public class AccountWorkScenario : IScenario
{
    private readonly AccountService _accountService;

    public AccountWorkScenario(AccountService accountService)
    {
        _accountService = accountService;
    }

    public void Run()
    {
        string command = AnsiConsole.Ask<string>("What would you like to do? : 1 - Refill an account; 2 - Withdraw money; 3 - Check balance; 4 - Show Transanctions; 0 - Exit");

        switch (command)
        {
            case "1":
            {
                int deposit = AnsiConsole.Ask<int>("Enter the deposit amount: ");
                _accountService.Refill(deposit);
                AnsiConsole.WriteLine("[green]Deposit was successful![/]");
                break;
            }

            case "2":
            {
                int withdraw = AnsiConsole.Ask<int>("Enter the withdraw amount: ");
                _accountService.Withdraw(withdraw);
                AnsiConsole.WriteLine("[green]Withdraw was successful![/]");
                break;
            }

            case "3":
            {
                double balance = _accountService.CheckBalance();
                AnsiConsole.WriteLine($"Current balance: {balance}");
                break;
            }

            case "4":
            {
                Func<IEnumerable<Transaction>> transactionHistory = _accountService.GetTransactionHistory;
                IEnumerable<Transaction> transactions = transactionHistory();
                foreach (Transaction transaction in transactions)
                {
                    AnsiConsole.WriteLine($"{transaction.Type} - {transaction.Amount}");
                }

                break;
            }

            case "0":
            {
                break;
            }

            default:
            {
                throw new ArgumentException("Invalid command");
            }
        }
    }
}