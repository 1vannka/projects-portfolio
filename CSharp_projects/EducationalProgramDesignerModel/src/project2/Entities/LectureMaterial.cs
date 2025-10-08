public class LectureMaterial : ILectureMaterial
{
    public LectureMaterial(string title, string shortDescription, string content, User author, Guid? originalLectureMaterialId)
    {
        Id = Guid.NewGuid();
        Title = title;
        ShortDescription = shortDescription;
        Content = content;
        Author = author;
        OriginalLectureMaterialId = originalLectureMaterialId;
    }

    public Guid Id { get; }

    public string Title { get;  set; }

    public string ShortDescription { get;  set; }

    public string Content { get;  set; }

    public User Author { get; set; }

    public Guid? OriginalLectureMaterialId { get;  }
}