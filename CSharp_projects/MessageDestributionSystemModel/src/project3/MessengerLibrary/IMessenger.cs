public interface IMessenger
{
    string Opening { get; }

    void SendMessage(string text);
}