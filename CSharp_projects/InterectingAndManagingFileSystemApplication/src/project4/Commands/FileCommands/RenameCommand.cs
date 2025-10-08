public class RenameCommand : ICommand
{
    private readonly string _path;
    private readonly string _newName;

    public RenameCommand(string path, string newName)
    {
        _path = path;
        _newName = newName;
    }

    public void Run()
    {
        string newPath = Path.Combine(Path.GetDirectoryName(_path) ?? throw new InvalidOperationException(), _newName);
        File.Move(_path, newPath);
    }
}