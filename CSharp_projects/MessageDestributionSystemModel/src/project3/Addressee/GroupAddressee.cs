public class GroupAddressee : IAddressee
{
    public GroupAddressee()
    {
        _addresses = new List<IAddressee>();
    }

    private readonly List<IAddressee> _addresses;

    public void AddAddresse(IAddressee addresse)
    {
        _addresses.Add(addresse);
    }

    public void GetMessage(Message message)
    {
        foreach (IAddressee addresse in _addresses)
        {
            addresse.GetMessage(message);
        }
    }
}