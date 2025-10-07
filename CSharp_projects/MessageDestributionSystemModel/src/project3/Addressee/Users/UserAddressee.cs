public class UserAddressee : IAddressee
{
    public UserAddressee(User user)
    {
        User = user;
        messages = new List<Message>();
        Logger = new Logger();
    }

    public User User { get; protected set; }

    public ILogger Logger { get; set; }

    private readonly List<Message> messages;

    public void GetMessage(Message message)
    {
            messages.Add(message);
            message.ReadStatus = "UnRead";
            Logger.Log($"Message is received");
    }

    public EditStatusResult ReadMessage(Message message)
    {
        if (message.ReadStatus == "Read")
        {
            return new EditStatusResult.IsAlreadyRead();
        }

        message.ReadStatus = "Read";

        Logger.Log($"Message read: {message.Header}");

        return new EditStatusResult.Success();
    }
}