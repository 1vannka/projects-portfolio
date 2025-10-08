public class ListCommand : ICommand
{
    private readonly int _depth;

    private readonly string _path;

    public ListCommand(int depth, string path)
    {
        _depth = depth;
        _path = path;
    }

    public void Run()
    {
        if (_depth > 0)
        {
            var directoryInfo = new DirectoryInfo(_path);
            FileInfo[] files = directoryInfo.GetFiles();

            for (int i = 0; i <= _depth; i++)
            {
                Console.WriteLine($"{i + 1}. {files[i].Name}");
            }
        }
    }
}