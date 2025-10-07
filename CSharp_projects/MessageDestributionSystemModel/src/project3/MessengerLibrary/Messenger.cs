public class Messenger : IMessenger
{
    public string Opening { get; } = "Messenger";

    public void SendMessage(string text)
    {
        Console.WriteLine($"{Opening}: {text}");
    }
}