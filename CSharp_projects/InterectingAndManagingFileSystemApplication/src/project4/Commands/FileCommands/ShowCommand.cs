public class ShowCommand : ICommand
{
    public ShowCommand(string path, string flag, string mode)
    {
        Path = path;
        Mode = mode;
        Flag = flag;
    }

    public string Path { get; set; }

    public string Mode { get; set; }

    public string Flag { get; set; }

    public void Run()
    {
        if (!File.Exists(Path))
        {
            Console.WriteLine($" File {Path} is not exists");
            return;
        }

        if (Mode == "console")
        {
            string content = File.ReadAllText(Path);
            Console.WriteLine($"Content of file {Path}:\n{content}");
        }
        else
        {
            Console.WriteLine("Not supported format");
        }
    }
}