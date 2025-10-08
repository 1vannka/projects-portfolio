public interface IMessage
{
    public string? Header { get; }

    public string? Body { get; }

    public PriorityLevel Priority { get; }
}