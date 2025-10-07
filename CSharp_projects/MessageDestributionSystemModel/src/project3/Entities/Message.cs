public class Message : IMessage
{
    public Message(string? header, string? body, PriorityLevel priority)
    {
        Header = header;
        Body = body;
        Priority = priority;
    }

    public string? Header { get; }

    public string? Body { get; }

    public string? ReadStatus { get; set; }

    public PriorityLevel Priority { get; }
}
