public interface ICommandFactory
{
    ICommand CreateCommand(string[] args);
}