public class DeleteHandler : IHandler
{
    public DeleteHandler(IFileManager? fileManager)
    {
        NextHandler = new MoveHandler(fileManager);
        FileManager = fileManager;
    }

    public IHandler? NextHandler { get; }

    public IFileManager? FileManager { get; set; }

    public void Execute(string[] args)
    {
        if (args[0] == "file" && args[1] == "delete")
        {
            FileManager?.DeleteFile(args[2]);
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