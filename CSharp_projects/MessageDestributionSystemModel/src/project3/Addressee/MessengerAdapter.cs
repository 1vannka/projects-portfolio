public class MessengerAdapter : IAddressee
{
    private readonly IMessenger _messenger;

    public MessengerAdapter(IMessenger messenger)
    {
        _messenger = messenger;
    }

    public void GetMessage(Message message)
    {
        _messenger.SendMessage($"{message.Header}: {message.Body}");
    }
}