public class TreeGoToCommandFactory : ICommandFactory
{
    public ICommand CreateCommand(string[] args)
    {
        return new GoToCommand(args[0]);
    }
}