public class ConnectCommand : ICommand
{
    public ConnectCommand(string path, string mode)
    {
        Path = path;
        Mode = mode;
    }

    public string Path { get; private set; }

    public string Mode { get; private set; }

    public void Run()
    {
        Console.WriteLine($"Connected to file system : {Mode}");
        Console.WriteLine($"Path: {Path}");
    }
}