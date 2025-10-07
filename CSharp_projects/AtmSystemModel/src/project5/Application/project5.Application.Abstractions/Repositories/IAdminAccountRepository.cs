using Lab5.Application.Models.Accounts;

namespace Lab5.Application.Abstractions.Repositories;

public interface IAdminAccountRepository
{
    public bool FindAdminByPassword(string password);

    public AdminAccount? GetAdminAccountByPassword(string password);
}