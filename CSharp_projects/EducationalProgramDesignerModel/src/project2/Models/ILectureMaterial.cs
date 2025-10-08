public interface ILectureMaterial
{
    public Guid Id { get; }

    public string Title { get;  set; }

    public string ShortDescription { get;  set; }

    public string Content { get;  set; }

    public User Author { get; set; }

    public Guid? OriginalLectureMaterialId { get;  }
}