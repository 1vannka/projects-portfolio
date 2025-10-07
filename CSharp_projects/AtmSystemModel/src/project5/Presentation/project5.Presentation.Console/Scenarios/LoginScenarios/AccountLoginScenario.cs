using Lab5.Application.Accounts;
using Lab5.Application.Contracts;
using Spectre.Console;

namespace Lab5.Presentation.Console.Scenarios.LoginScenarios;

public class AccountLoginScenario : IScenario
{
    private readonly AccountService _accountService;

    public AccountLoginScenario(AccountService accountService)
    {
        _accountService = accountService;
    }

    public void Run()
    {
        long number = AnsiConsole.Ask<long>("Enter your account number: ");
        string pin = AnsiConsole.Ask<string>("Enter your account pin: ");
        LoginResult result = _accountService.Login(number, pin);
        if (result is LoginResult.Success)
        {
            AnsiConsole.WriteLine("Login successful");
            var accountWorkScenario = new AccountWorkScenario(_accountService);
            accountWorkScenario.Run();
        }
        else
        {
            throw new ArgumentOutOfRangeException(nameof(result));
        }
    }
}