public class ConnectHandler : IHandler
{
    public ConnectHandler()
    {
        NextHandler = new DisconnectHandler(FileManager);
        FileManager = new LocalFileManager();
    }

    public IHandler? NextHandler { get; }

    public IFileManager? FileManager { get; set; }

    public void Execute(string[] args)
    {
        if (args[0] == "connect")
        {
            if (FileManager != null)
            {
                FileManager.Connect(args[1], args[2]);
                FileManager.Directory = args[1];
                if (NextHandler != null)
                {
                    NextHandler.FileManager = FileManager;
                }
            }
        }
        else
        {
            if (NextHandler != null)
            {
                NextHandler.Execute(args);
            }
        }
    }
}