public class Test
{
    [Fact]
    public void ExecuteConnectCommand()
    {
        var commandParser = new CommandParser();
        string[] input = { "connect", "path/to/test/file", "local" };
        var expectedCommand = new ConnectCommand("path/to/test/file", "local");
        commandParser.Execute(input);

        if (commandParser.FileManager != null)
        {
            Stack<ICommand> commands = commandParser.FileManager.Commands;
            ICommand actualCommand = commands.Peek();

            Assert.IsType<ConnectCommand>(actualCommand);
            if (actualCommand is ConnectCommand connectCommand)
            {
                Assert.Equal(expectedCommand.Mode, connectCommand.Mode);
                Assert.Equal(expectedCommand.Path, connectCommand.Path);
            }
        }
    }

    [Fact]
    public void ExecuteDisconnectCommand()
    {
        var commandParser = new CommandParser();
        string[] input = { "connect", "path/to/test/file", "local" };
        commandParser.Execute(input);
        string[] input2 = { "disconnect", "path/to/test/file", "local" };
        commandParser.Execute(input2);

        if (commandParser.FileManager != null)
        {
            Stack<ICommand> commands = commandParser.FileManager.Commands;
            ICommand actualCommand = commands.Peek();

            Assert.IsType<DisconnectCommand>(actualCommand);
        }
    }

    [Fact]
    public void ExecuteTreeGoToCommand()
    {
        var commandParser = new CommandParser();
        string[] input = { "connect", "path/to/test/file", "local" };
        commandParser.Execute(input);
        string[] input2 = { "tree", "goto", "path/to/test/file" };
        commandParser.Execute(input2);
        var expectedCommand = new GoToCommand("path/to/test/file");

        if (commandParser.FileManager != null)
        {
            Stack<ICommand> commands = commandParser.FileManager.Commands;
            ICommand actualCommand = commands.Peek();

            Assert.IsType<GoToCommand>(actualCommand);
            if (actualCommand is GoToCommand goToCommand)
            {
                Assert.Equal(expectedCommand.Path, goToCommand.Path);
            }
        }
    }

    [Fact]
    public void ExecuteShowCommand()
    {
        var commandParser = new CommandParser();
        string[] input = { "connect", "path/to/test/file", "local" };
        commandParser.Execute(input);
        string[] input2 = { "file", "show", "path/to/test/file", "-d", "console" };
        commandParser.Execute(input2);
        var expectedCommand = new ShowCommand("path/to/test/file", "-d", "console");

        if (commandParser.FileManager != null)
        {
            Stack<ICommand> commands = commandParser.FileManager.Commands;
            ICommand actualCommand = commands.Peek();

            Assert.IsType<ShowCommand>(actualCommand);
            if (actualCommand is ShowCommand showCommand)
            {
                Assert.Equal(expectedCommand.Path, showCommand.Path);
                Assert.Equal(expectedCommand.Mode, showCommand.Mode);
            }
        }
    }
}