public class GoToCommand : ICommand
{
    public GoToCommand(string path)
    {
        Path = path;
    }

    public string Path { get; set; }

    public void Run()
    {
        Console.WriteLine($"New directory : {Path}");
    }
}