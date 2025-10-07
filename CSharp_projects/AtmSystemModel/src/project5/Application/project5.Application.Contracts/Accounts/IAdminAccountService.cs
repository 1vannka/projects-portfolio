namespace Lab5.Application.Contracts.Accounts;

public interface IAdminAccountService
{
    LoginResult Login(string password);
}