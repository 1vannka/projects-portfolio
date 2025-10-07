public class UserRepository
{
    public UserRepository()
    {
        Users = new List<User>();
    }

    private List<User> Users { get; set; }

    public void AddUser(User user)
    {
        Users.Add(user);
    }

    public User FindUser(Guid userId)
    {
        foreach (User user in Users)
        {
            if (user.Id == userId)
            {
                return user;
            }
        }

        throw new Exception("There is no such User");
    }
}