public interface ILaboratoryWork
{
    public Guid Id { get; }

    public string Title { get;  set; }

    public string Description { get;  set; }

    public string GradeCriteria { get; }

    public int Points { get;  set; }

    public User Author { get; set; }

    public Guid? OriginalLaboratoryWorkId { get;  }
}