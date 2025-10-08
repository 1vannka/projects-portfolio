public class FileDisplay : IDisplayDriver
{
    public FileDisplay(string filename)
    {
        FileName = filename;
    }

    private string FileName { get; }

    public void Clear()
    {
        File.WriteAllText(FileName, string.Empty);
    }

    public void SetColor(byte r, byte g, byte b)
    {
        File.AppendAllText(FileName, $"Color in RGB: {r}, {g}, {b}\n");
    }

    public void ShowText(string text)
    {
        File.AppendAllText(FileName, text);
    }
}