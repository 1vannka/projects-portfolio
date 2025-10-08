using Lab5.Application.Abstractions.Repositories;
using Lab5.Application.Models.Accounts;
using Npgsql;

namespace Lab5.Infrastructure.DataAccess.DataBaseRepositories;

public class AccountRepository : IAccountRepository
{
    private readonly string _connectionString;

    public AccountRepository(string connectionString)
    {
        _connectionString = connectionString;
    }

    public void CreateAccount(int number, string password)
    {
        using var connection = new NpgsqlConnection(_connectionString);
        connection.Open();
        var command = new NpgsqlCommand("INSERT INTO accounts (account_id, account_number, account_pin, account_balance) VALUES (@id, @number, @pin, @balance)", connection);
        command.Parameters.AddWithValue("@number", number);
        command.Parameters.AddWithValue("@pin", password);
        command.Parameters.AddWithValue("@balance", 0);
        command.ExecuteNonQuery();
    }

    public void UpdateAccount(Account account)
    {
        using var connection = new NpgsqlConnection(_connectionString);
        connection.Open();
        var command = new NpgsqlCommand("UPDATE accounts SET account_balance = @balance WHERE account_number = @number", connection);
        command.Parameters.AddWithValue("@balance", account.Balance);
        command.Parameters.AddWithValue("@number", account.Number);
        command.ExecuteNonQuery();
    }

    public Account? FindAccountByNumber(long number)
    {
        var connection = new NpgsqlConnection(_connectionString);
        connection.Open();

        var command = new NpgsqlCommand("select * from accounts where number = @number", connection);

        command.Parameters.AddWithValue("@number", number);

        using NpgsqlDataReader reader = command.ExecuteReader();

        if (reader.Read() is false)
        {
            return null;
        }

        return new Account(reader.GetInt32(1), reader.GetString(2));
    }
}