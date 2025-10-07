public class LocalFileManager : IFileManager
{
    public string? Directory { get; set; } = null;

    public Stack<ICommand> Commands { get; set; } = new Stack<ICommand>();

    public void Connect(string path, string mode)
    {
        Directory = path;
        string[] args = [path, mode];
        var commandFactory = new ConnectCommandFactory();
        ICommand command = commandFactory.CreateCommand(args);
        command.Run();
        Commands.Push(new ConnectCommand(path, mode));
    }

    public void Disconnect()
    {
        string[]? args = Array.Empty<string>();
        var commandFactory = new DisconnectCommandFactory();
        ICommand command = commandFactory.CreateCommand(args);
        command.Run();
        Commands.Push(new DisconnectCommand());
    }

    public void GotoPath(string path)
    {
        string[] args = [path];
        var commandFactory = new TreeGoToCommandFactory();
        ICommand command = commandFactory.CreateCommand(args);
        command.Run();
        Commands.Push(new GoToCommand(path));
    }

    public void ListTree(int depth)
    {
        if (Directory != null)
        {
            string[] args = [depth.ToString(), Directory];
            var commandFactory = new TreeListCommandFactory();
            ICommand command = commandFactory.CreateCommand(args);
            command.Run();
            Commands.Push(new ListCommand(depth, Directory));
        }
    }

    public void CopyFile(string sourcePath, string destinationPath)
    {
        string[] args = [sourcePath, destinationPath];
        var commandFactory = new FileCopyCommandFactory();
        ICommand command = commandFactory.CreateCommand(args);
        command.Run();
        Commands.Push(new CopyCommand(sourcePath, destinationPath));
    }

    public void DeleteFile(string path)
    {
        string[] args = [path];
        var commandFactory = new FileDeleteCommandFactory();
        ICommand command = commandFactory.CreateCommand(args);
        command.Run();
        Commands.Push(new DeleteCommand(path));
    }

    public void MoveFile(string sourcePath, string destinationPath)
    {
        string[] args = [sourcePath, destinationPath];
        var commandFactory = new FileMoveCommandFactory();
        ICommand command = commandFactory.CreateCommand(args);
        command.Run();
        Commands.Push(new MoveCommand(sourcePath, destinationPath));
    }

    public void RenameFile(string path, string newName)
    {
        string[] args = [path, newName];
        var commandFactory = new RenameCommandFactory();
        ICommand command = commandFactory.CreateCommand(args);
        command.Run();
        Commands.Push(new RenameCommand(path, newName));
    }

    public void ShowFile(string path, string flag, string mode)
    {
        string[] args = [path, flag, mode];
        var commandFactory = new FileShowCommandFactory();
        ICommand command = commandFactory.CreateCommand(args);
        command.Run();
        Commands.Push(new ShowCommand(path, flag, mode));
    }
}