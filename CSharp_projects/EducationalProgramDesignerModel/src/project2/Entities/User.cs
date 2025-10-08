public class User : IUser
{
    public User(string name)
    {
        Id = Guid.NewGuid();
        Name = name;
    }

    public Guid Id { get; set; }

    public string Name { get; set; }
}