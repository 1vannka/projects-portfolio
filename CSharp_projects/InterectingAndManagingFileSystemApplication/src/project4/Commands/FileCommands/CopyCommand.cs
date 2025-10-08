public class CopyCommand : ICommand
{
    public CopyCommand(string sourcePath, string destinationPath)
    {
        SourcePath = sourcePath;
        DestinationPath = destinationPath;
    }

    public string SourcePath { get; set; }

    public string DestinationPath { get; set; }

    public void Run()
    {
        File.Copy(SourcePath, DestinationPath);
    }
}