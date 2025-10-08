using Lab5.Application.Abstractions.Repositories;
using Lab5.Application.Models.Accounts;
using Npgsql;

namespace Lab5.Infrastructure.DataAccess.DataBaseRepositories;

public class AdminAccountRepository : IAdminAccountRepository
{
    private readonly string _connectionString;

    public AdminAccountRepository(string connectionString)
    {
        _connectionString = connectionString;
    }

    public bool FindAdminByPassword(string password)
    {
        var connection = new NpgsqlConnection(_connectionString);
        connection.Open();

        var command = new NpgsqlCommand("select password from admins where password = @password", connection);

        command.Parameters.AddWithValue("@password", password);

        using NpgsqlDataReader reader = command.ExecuteReader();

        if (reader.Read() is false)
        {
            return false;
        }

        return true;
    }

    public AdminAccount? GetAdminAccountByPassword(string password)
    {
        var connection = new NpgsqlConnection(_connectionString);
        connection.Open();

        var command = new NpgsqlCommand("select password from admins where password = @password", connection);

        command.Parameters.AddWithValue("@password", password);

        using NpgsqlDataReader reader = command.ExecuteReader();

        if (reader.Read() is false)
        {
            return null;
        }

        return new AdminAccount(reader.GetString(1));
    }
}