public class AddresseeProxy : IAddressee
{
    private readonly IAddressee _addressee;

    private readonly PriorityLevel? _priorityLevel;

    public AddresseeProxy(IAddressee addressee, PriorityLevel? priority)
    {
        _addressee = addressee;
        _priorityLevel = priority;
    }

    public void GetMessage(Message message)
    {
        if (message.Priority <= _priorityLevel || _priorityLevel == null)
        {
            _addressee.GetMessage(message);
        }
    }
}