public class GoToHandler : IHandler
{
    public GoToHandler(IFileManager? fileManager)
    {
        NextHandler = new ListHandler(FileManager);
        FileManager = fileManager;
    }

    public IFileManager? FileManager { get; set; }

    public IHandler? NextHandler { get; }

    public void Execute(string[] args)
    {
        if (args[0] == "tree" && args[1] == "goto")
        {
            FileManager?.GotoPath(args[2]);
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