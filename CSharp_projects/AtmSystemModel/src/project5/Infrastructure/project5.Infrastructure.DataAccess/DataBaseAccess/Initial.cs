using Npgsql;

namespace Lab5.Infrastructure.DataAccess.DataBaseAccess;

public class Initial
{
    private readonly string _connectionString;

    public Initial(string connectionString)
    {
        _connectionString = connectionString;
    }

    public void Initialize()
    {
        var connection = new NpgsqlConnection(_connectionString);
        connection.Open();

        const string createAccountsTableCommand = @"CREATE TABLE IF NOT EXISTS accounts ( account_id UUID Primary KEY,account_number integer NOT NULL,account_password varchar NOT NULL,account_balance double precision NOT NULL)";

        const string createAdminAccountsTableCommand =
            @"CREATE TABLE IF NOT EXISTS adminAccounts( account_id UUID Primary Key, password varchar NOT NULL)";

        const string createTransactionsTableCommand =
            @"CREATE TABLE IF NOT EXISTS transactions ( transaction_type varchar not null,amount int)";

        using var command = new NpgsqlCommand(createAccountsTableCommand, connection);
        command.ExecuteNonQuery();

        using var command2 = new NpgsqlCommand(createAdminAccountsTableCommand, connection);
        command2.ExecuteNonQuery();

        using var command3 = new NpgsqlCommand(createTransactionsTableCommand, connection);
        command3.ExecuteNonQuery();
    }
}