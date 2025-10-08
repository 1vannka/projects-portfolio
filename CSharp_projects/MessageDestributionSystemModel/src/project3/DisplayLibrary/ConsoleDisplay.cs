public class ConsoleDisplay : IDisplayDriver
{
    public void Clear()
    {
        Console.Clear();
    }

    public void SetColor(byte r, byte g, byte b)
    {
        Console.WriteLine(Output.Rgb(r, g, b));
    }

    public void ShowText(string text)
    {
        Console.WriteLine(text);
    }
}