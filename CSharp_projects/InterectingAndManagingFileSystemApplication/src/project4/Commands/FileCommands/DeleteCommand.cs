public class DeleteCommand : ICommand
{
    private readonly string _path;

    public DeleteCommand(string path)
    {
        _path = path;
    }

    public void Run()
    {
        File.Delete(_path);
    }
}