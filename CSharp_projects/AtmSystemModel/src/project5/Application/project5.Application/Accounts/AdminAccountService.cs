using Lab5.Application.Abstractions.Repositories;
using Lab5.Application.Contracts;
using Lab5.Application.Contracts.Accounts;
using Lab5.Application.Models.Accounts;

namespace Lab5.Application.Accounts;

public class AdminAccountService : IAdminAccountService
{
    private readonly IAdminAccountRepository _adminAccountRepository;

    public AdminAccountService(IAdminAccountRepository adminAccountRepository)
    {
        _adminAccountRepository = adminAccountRepository;
        Account = null;
    }

    public AdminAccount? Account { get; set; }

    public LoginResult Login(string password)
    {
        bool result = _adminAccountRepository.FindAdminByPassword(password);
        if (result)
        {
            Account = _adminAccountRepository.GetAdminAccountByPassword(password);

            return new LoginResult.Success();
        }

        return new LoginResult.NotFound();
    }

    public void ChangePassword(string newPassword)
    {
        if (Account != null)
        {
            Account.Password = newPassword;
        }
    }

    public AdminAccount? GetAdminAccount(string password)
    {
        AdminAccount? account = _adminAccountRepository.GetAdminAccountByPassword(password);
        return account;
    }
}