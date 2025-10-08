public class ConnectCommandFactory : ICommandFactory
{
    public ICommand CreateCommand(string[] args)
    {
        return new ConnectCommand(args[0], args[1]);
    }
}