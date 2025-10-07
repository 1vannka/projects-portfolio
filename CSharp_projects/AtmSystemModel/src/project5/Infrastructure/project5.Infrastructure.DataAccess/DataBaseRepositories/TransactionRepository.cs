using Lab5.Application.Abstractions.Repositories;
using Lab5.Application.Models.Transactions;
using Npgsql;

namespace Lab5.Infrastructure.DataAccess.Repositories;

public class TransactionRepository : ITransactionRepository
{
    private readonly string _connectionString;

    public TransactionRepository(string connectionString)
    {
        _connectionString = connectionString;
    }

    public void CreateTransaction(Transaction transaction)
    {
        using var connection = new NpgsqlConnection(_connectionString);
        connection.Open();
        var command = new NpgsqlCommand("INSERT INTO transactions(transaction_type, amount) Values(@type, @amount)", connection);
        command.Parameters.AddWithValue("@type", transaction.Type.ToString());
        if (transaction.Amount != null)
        {
            command.Parameters.AddWithValue("@amount", transaction.Amount);
        }
        else
        {
            command.Parameters.AddWithValue("@amount", 0);
        }
    }

    public IEnumerable<Transaction> GetAll()
    {
        using var connection = new NpgsqlConnection(_connectionString);
        connection.Open();
        var command = new NpgsqlCommand("SELECT * from transactions", connection);

        using NpgsqlDataReader reader = command.ExecuteReader();

        var transactions = new List<Transaction>();

        if (reader.Read() is false)
        {
            return new List<Transaction>();
        }

        while (reader.Read())
        {
            string type = reader.GetString(0);

            var transaction = new Transaction(Enum.Parse<TransactionType>(type), reader.GetInt32(reader.GetOrdinal("Amount")));

            transactions.Add(transaction);
        }

        return transactions;
    }
}