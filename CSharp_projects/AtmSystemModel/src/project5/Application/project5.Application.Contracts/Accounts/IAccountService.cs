namespace Lab5.Application.Contracts.Accounts;

public interface IAccountService
{
    LoginResult Login(long number, string password);
}