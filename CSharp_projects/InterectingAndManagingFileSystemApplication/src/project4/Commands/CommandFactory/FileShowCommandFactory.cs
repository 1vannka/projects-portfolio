public class FileShowCommandFactory : ICommandFactory
{
    public ICommand CreateCommand(string[] args)
    {
        return new ShowCommand(args[0], args[1], args[2]);
    }
}