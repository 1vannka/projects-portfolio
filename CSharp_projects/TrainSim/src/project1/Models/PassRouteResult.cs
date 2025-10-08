public abstract record PassRouteResult
{
    private PassRouteResult() { }

    public sealed record Success : PassRouteResult;

    public sealed record FailToPassRoute : PassRouteResult;

    public sealed record FailToStop : PassRouteResult;
}