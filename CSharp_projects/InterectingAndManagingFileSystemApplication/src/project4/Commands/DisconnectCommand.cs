public class DisconnectCommand : ICommand
{
    public void Run()
    {
        Console.WriteLine("Disconnected from file system.");
    }
}