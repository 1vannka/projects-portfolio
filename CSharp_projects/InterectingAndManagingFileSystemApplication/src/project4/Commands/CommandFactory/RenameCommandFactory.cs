public class RenameCommandFactory : ICommandFactory
{
    public ICommand CreateCommand(string[] args)
    {
        return new RenameCommand(args[0], args[1]);
    }
}