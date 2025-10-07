public class MoveHandler : IHandler
{
    public MoveHandler(IFileManager? fileManager)
    {
        NextHandler = new RenameHandler(fileManager);
        FileManager = fileManager;
    }

    public IHandler? NextHandler { get; }

    public IFileManager? FileManager { get; set; }

    public void Execute(string[] args)
    {
        if (args[0] == "file" && args[1] == "move")
        {
            FileManager?.MoveFile(args[2], args[3]);
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