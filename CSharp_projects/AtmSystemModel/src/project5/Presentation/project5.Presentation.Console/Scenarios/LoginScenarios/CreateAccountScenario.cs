using Lab5.Application.Abstractions.Repositories;
using Lab5.Application.Models.Accounts;
using Spectre.Console;

namespace Lab5.Presentation.Console.Scenarios.LoginScenarios;

public class CreateAccountScenario
{
   private readonly IAccountRepository _accountRepository;

   public CreateAccountScenario(IAccountRepository accountRepository)
   {
       _accountRepository = accountRepository;
   }

   public Account Run()
   {
        int number = AnsiConsole.Ask<int>("Enter number:");
        string password = AnsiConsole.Ask<string>("Enter password:");
        var account = new Account(number, password);

        _accountRepository.CreateAccount(number, password);

        return account;
   }
}