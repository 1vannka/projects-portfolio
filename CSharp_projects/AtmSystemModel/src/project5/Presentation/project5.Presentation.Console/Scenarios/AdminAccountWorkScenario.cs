using Lab5.Application.Accounts;
using Spectre.Console;

namespace Lab5.Presentation.Console.Scenarios;

public class AdminAccountWorkScenario : IScenario
{
    private readonly AdminAccountService _adminAccountService;

    public AdminAccountWorkScenario(AdminAccountService adminAccountService)
    {
        _adminAccountService = adminAccountService;
    }

    public void Run()
    {
        string command = AnsiConsole.Ask<string>("What would you like to do?: 1 - change system password; 0 - exit ");

        switch (command)
        {
            case "1":
            {
                string newPassword = AnsiConsole.Ask<string>("Enter new password: ");
                _adminAccountService.ChangePassword(newPassword);
                AnsiConsole.WriteLine("[green]Password change was successful![/]");
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