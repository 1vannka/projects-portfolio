public abstract record EditResult
{
    private EditResult() { }

    public sealed record Success : EditResult;

    public sealed record FailToEdit : EditResult;
}