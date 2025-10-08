public class FileDeleteCommandFactory : ICommandFactory
{
    public ICommand CreateCommand(string[] args)
    {
        return new DeleteCommand(args[0]);
    }
}