using Lab5.Application.Abstractions.Repositories;
using Lab5.Application.Accounts;
using Lab5.Presentation.Console.Scenarios.LoginScenarios;
using Spectre.Console;

namespace Lab5.Presentation.Console.Scenarios;

public class LoginScenario : IScenario
{
    private readonly AccountLoginScenario _accountLoginScenario;

    private readonly AdminAccountLoginScenario _adminAccountLoginSceanrio;

    private readonly IAccountRepository _accountRepository;

    private readonly IAdminAccountRepository _adminAccountRepository;

    private readonly CreateAccountScenario _createAccountScenario;

    public LoginScenario(IAccountRepository accountRepository, IAdminAccountRepository adminAccountRepository)
    {
        _accountRepository = accountRepository;
        _adminAccountRepository = adminAccountRepository;
        _accountLoginScenario = new AccountLoginScenario(new AccountService(_accountRepository));
        _adminAccountLoginSceanrio = new AdminAccountLoginScenario(new AdminAccountService(_adminAccountRepository));
        _createAccountScenario = new CreateAccountScenario(_accountRepository);
    }

    public void Run()
    {
        string scenario = AnsiConsole.Ask<string>("What do you prefer to do?: 1 - Login in account; 2 - Login as Admin; 3 - Create account; 4 - Exit");

        switch (scenario)
        {
            case "1":
                _accountLoginScenario.Run();
                break;

            case "2":
                _adminAccountLoginSceanrio.Run();

                break;

            case "3":
                _createAccountScenario.Run();
                break;

            case "4":
                break;

            default:
                throw new Exception("Invalid option");
        }
    }
}