public abstract record CreateEntityResult
{
    private CreateEntityResult() { }

    public sealed record Success : CreateEntityResult;

    public sealed record FailToCreate : CreateEntityResult;
}