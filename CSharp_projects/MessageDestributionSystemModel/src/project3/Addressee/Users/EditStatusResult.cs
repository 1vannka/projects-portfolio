public abstract record EditStatusResult
{
    private EditStatusResult() { }

    public sealed record Success : EditStatusResult;

    public sealed record IsAlreadyRead : EditStatusResult;
}