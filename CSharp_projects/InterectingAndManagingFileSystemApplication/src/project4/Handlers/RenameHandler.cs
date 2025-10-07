public class RenameHandler : IHandler
{
    public RenameHandler(IFileManager? fileManager)
    {
        FileManager = fileManager;
        NextHandler = new ShowHandler(fileManager);
    }

    public IHandler? NextHandler { get; }

    public IFileManager? FileManager { get; set; }

    public void Execute(string[] args)
    {
        if (args[0] == "file" && args[1] == "rename")
        {
            FileManager?.RenameFile(args[2], args[3]);
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