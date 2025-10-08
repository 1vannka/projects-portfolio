public class ShowHandler : IHandler
{
    public ShowHandler(IFileManager? fileManager)
    {
        FileManager = fileManager;
        NextHandler = null;
    }

    public IHandler? NextHandler { get; }

    public IFileManager? FileManager { get; set; }

    public void Execute(string[] args)
    {
        if (args[0] == "file" && args[1] == "show")
        {
            if (FileManager != null)
            {
                FileManager.ShowFile(args[2], args[3], args[4]);
            }
        }
        else
        {
            throw new Exception("Invalid command");
        }
    }
}