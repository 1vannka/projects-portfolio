using Lab5.Application.Accounts;
using Lab5.Application.Contracts;
using Lab5.Application.Models.Accounts;
using Spectre.Console;

namespace Lab5.Presentation.Console.Scenarios.LoginScenarios;

public class AdminAccountLoginScenario : IScenario
{
    private readonly AdminAccountService _adminAccountService;

    public AdminAccountLoginScenario(AdminAccountService adminAccountService)
    {
        _adminAccountService = adminAccountService;
    }

    public void Run()
    {
        string password = AnsiConsole.Ask<string>("Enter password: ");
        LoginResult result = _adminAccountService.Login(password);
        AdminAccount? account = _adminAccountService.GetAdminAccount(password);

        if (result is LoginResult.Success)
        {
            AnsiConsole.WriteLine("Successfully logged in.");
            _adminAccountService.Account = account;
            var adminAccountWorkScenario = new AdminAccountWorkScenario(_adminAccountService);
            adminAccountWorkScenario.Run();
        }
        else
        {
            throw new ArgumentException("Invalid password.");
        }
    }
}