public class ListHandler : IHandler
{
    public ListHandler(IFileManager? fileManager)
    {
        NextHandler = new CopyHandler(fileManager);
        FileManager = fileManager;
    }

    public IHandler? NextHandler { get; }

    public IFileManager? FileManager { get; set; }

    public void Execute(string[] args)
    {
        if (args[0] == "tree" && args[1] == "list")
        {
            if (args[2] == "-d")
            {
                FileManager?.ListTree(int.Parse(args[3]));
            }

            FileManager?.ListTree(1);
        }
        else
        {
            if (NextHandler != null)
            {
                NextHandler.FileManager = FileManager;
                NextHandler.Execute(args);
            }
        }
    }
}