public class Display : IAddressee
{
    public Display(byte r, byte g, byte b, IDisplayDriver displayDriver)
    {
        R = r;
        G = g;
        B = b;
        _displayDriver = displayDriver;
    }

    private readonly IDisplayDriver _displayDriver;

    public byte R { get; }

    public byte G { get; }

    public byte B { get; }

    public void GetMessage(Message message)
    {
        _displayDriver.Clear();
        _displayDriver.SetColor(R, G, B);
        _displayDriver.ShowText($"{message.Header}: {message.Body}");
    }
}