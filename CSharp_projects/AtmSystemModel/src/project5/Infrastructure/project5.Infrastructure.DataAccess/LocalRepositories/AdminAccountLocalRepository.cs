using Lab5.Application.Abstractions.Repositories;
using Lab5.Application.Models.Accounts;

namespace Lab5.Infrastructure.DataAccess.LocalRepositories;

public class AdminAccountLocalRepository : IAdminAccountRepository
{
    private readonly IEnumerable<AdminAccount> _adminAccounts;

    public AdminAccountLocalRepository()
    {
        _adminAccounts = new List<AdminAccount>();
    }

    public bool FindAdminByPassword(string password)
    {
        foreach (AdminAccount adminAccount in _adminAccounts)
        {
            if (adminAccount.Password == password)
            {
                return true;
            }
        }

        return false;
    }

    public AdminAccount? GetAdminAccountByPassword(string password)
    {
        foreach (AdminAccount adminAccount in _adminAccounts)
        {
            if (adminAccount.Password == password)
            {
                return adminAccount;
            }
        }

        return null;
    }
}