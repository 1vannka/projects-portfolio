public class FileMoveCommandFactory : ICommandFactory
{
    public ICommand CreateCommand(string[] args)
    {
        return new MoveCommand(args[0], args[1]);
    }
}