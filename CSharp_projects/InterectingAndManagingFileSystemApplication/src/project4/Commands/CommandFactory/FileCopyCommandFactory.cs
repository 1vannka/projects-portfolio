public class FileCopyCommandFactory : ICommandFactory
{
    public ICommand CreateCommand(string[] args)
    {
        return new CopyCommand(args[0], args[1]);
    }
}