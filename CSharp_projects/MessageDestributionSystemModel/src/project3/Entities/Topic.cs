public class Topic
{
    public Topic(string title)
    {
        Title = title;
        _addresses = new List<IAddressee>();
        Logger = new Logger();
    }

    public string Title { get; protected set; }

    public ILogger Logger { get; set; }

    private readonly List<IAddressee> _addresses;

    public void AddAddresses(IAddressee addresse)
    {
        _addresses.Add(addresse);
    }

    public void SendMessage(Message message)
    {
        foreach (IAddressee addresse in _addresses)
        {
            addresse.GetMessage(message);
            Logger.Log($"Message is sent:{message.Header}");
        }
    }
}