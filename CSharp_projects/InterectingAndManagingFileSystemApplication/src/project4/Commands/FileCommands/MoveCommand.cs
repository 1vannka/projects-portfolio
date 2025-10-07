public class MoveCommand : ICommand
{
    private readonly string _sourcePath;
    private readonly string _destinationPath;

    public MoveCommand(string sourcePath, string destinationPath)
    {
        _sourcePath = sourcePath;
        _destinationPath = destinationPath;
    }

    public void Run()
    {
        File.Move(_sourcePath, _destinationPath);
    }
}