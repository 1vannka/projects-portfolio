public class Logger : ILogger
{
    public void Log(string log)
    {
        Console.WriteLine($"[LOG] {log}");
    }
}