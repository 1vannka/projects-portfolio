public class MessageBuilder
{
    private string? _header;
    private string? _body;
    private PriorityLevel _priority;

    public MessageBuilder SetHeader(string? header)
    {
        _header = header;
        return this;
    }

    public MessageBuilder SetBody(string? body)
    {
        _body = body;
        return this;
    }

    public MessageBuilder SetImportance(PriorityLevel priority)
    {
        _priority = priority;
        return this;
    }

    public Message Build()
    {
        return new Message(_header, _body, _priority);
    }
}