public class CommandParser : IParser
{
     public CommandParser()
    {
        FileManager = null;
    }

     public IFileManager? FileManager { get; set; }

     public ConnectHandler ConnectHandler { get; } = new ConnectHandler();

     public void Execute(string[] input)
    {
        ConnectHandler.Execute(input);
        FileManager = ConnectHandler.FileManager;
    }
}