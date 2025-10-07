public class CopyHandler : IHandler
{
    public CopyHandler(IFileManager? fileManager)
    {
        NextHandler = new DeleteHandler(fileManager);
        FileManager = fileManager;
    }

    public IHandler? NextHandler { get; }

    public IFileManager? FileManager { get; set; }

    public void Execute(string[] args)
    {
        if (args[0] == "file" && args[1] == "copy")
        {
            FileManager?.CopyFile(args[2], args[3]);
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