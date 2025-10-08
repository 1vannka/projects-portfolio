namespace Lab5.Application.Models.Accounts;

public class AdminAccount
{
    public AdminAccount(string password)
    {
        Password = password;
    }

    public string Password { get; set; }

    public Guid AdminId { get; set; }
}