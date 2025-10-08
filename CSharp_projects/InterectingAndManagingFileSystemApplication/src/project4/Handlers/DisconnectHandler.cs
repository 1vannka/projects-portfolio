public class DisconnectHandler : IHandler
{
    public DisconnectHandler(IFileManager? fileManager)
    {
        NextHandler = new GoToHandler(FileManager);
        FileManager = fileManager;
    }

    public IHandler? NextHandler { get; }

    public IFileManager? FileManager { get; set; }

    public void Execute(string[] args)
    {
        if (args[0] == "disconnect")
        {
            if (FileManager != null)
            {
                FileManager.Disconnect();
            }
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