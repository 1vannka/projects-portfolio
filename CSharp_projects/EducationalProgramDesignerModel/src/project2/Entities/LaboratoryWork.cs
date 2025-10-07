public class LaboratoryWork : ILaboratoryWork
{
    public LaboratoryWork(string title, string description, string gradeCriteria, int points, User author, Guid? originalLaboratoryWorkId)
    {
        Id = Guid.NewGuid();
        Title = title;
        Description = description;
        GradeCriteria = gradeCriteria;
        Points = points;
        Author = author;
        OriginalLaboratoryWorkId = originalLaboratoryWorkId;
        AuthorId = author.Id;
    }

    public Guid Id { get; }

    public string Title { get; set; }

    public string Description { get;  set; }

    public string GradeCriteria { get; }

    public Guid AuthorId { get; set; }

    public int Points { get; set; }

    public User Author { get; set; }

    public Guid? OriginalLaboratoryWorkId { get;  }
}