public interface ISubject
{
    public Guid Id { get; set; }

    public Guid? OriginalSubjectId { get; set; }

    public string Name { get; set; }

    public User Author { get; set; }

    public int SumPoints { get; set; }

    public int Points { get; set; }

    public LaboratoryWorkRepository LaboratoryWorks { get; set; }

    public LectureMaterialRepository LectureMaterials { get; set; }
}