public class TreeListCommandFactory : ICommandFactory
{
    public ICommand CreateCommand(string[] args)
    {
        return new ListCommand(int.Parse(args[0]), args[1]);
    }
}