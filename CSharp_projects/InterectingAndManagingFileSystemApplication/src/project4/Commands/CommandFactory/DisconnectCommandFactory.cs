public class DisconnectCommandFactory : ICommandFactory
{
    public ICommand CreateCommand(string[] args)
    {
        return new DisconnectCommand();
    }
}