using Lab5.Infrastructure.DataAccess.DataBaseRepositories;
using Lab5.Infrastructure.DataAccess.LocalRepositories;
using Spectre.Console;

namespace Lab5.Presentation.Console.Scenarios;

public class ChooseRepositoryScenario : IScenario
{
    private readonly AccountRepository _accountRepository;

    private readonly AdminAccountRepository _adminAccountRepository;

    public ChooseRepositoryScenario(AccountRepository accountRepository, AdminAccountRepository adminAccountRepository)
    {
        _accountRepository = accountRepository;
        _adminAccountRepository = adminAccountRepository;
    }

    public void Run()
    {
        string scenario = AnsiConsole.Ask<string>("What kind of repository?: 1 - DataBase; 2 - Local");

        switch (scenario)
        {
            case "1":
            {
                var loginScenario = new LoginScenario(_accountRepository, _adminAccountRepository);
                loginScenario.Run();
                break;
            }

            case "2":
            {
                var loginScenario = new LoginScenario(new AccountLocalRepository(), new AdminAccountLocalRepository());
                loginScenario.Run();
                break;
            }

            default:
            {
                throw new Exception("Invalid choice");
            }
        }
    }
}