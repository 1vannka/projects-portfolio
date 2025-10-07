public interface IHandler
{
    IHandler? NextHandler { get; }

    IFileManager? FileManager { get; set; }

    void Execute(string[] args);
}