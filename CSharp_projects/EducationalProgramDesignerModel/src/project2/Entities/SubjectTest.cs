public class SubjectTest : ISubject
{
    public SubjectTest(Guid? originalSubjectId, string name, User author, int minPoints)
    {
        Id = Id = Guid.NewGuid();
        OriginalSubjectId = originalSubjectId;
        Name = name;
        Author = author;
        MinPoints = minPoints;
        LaboratoryWorks = new LaboratoryWorkRepository();
        LectureMaterials = new LectureMaterialRepository();
        Points = 0;
        SumPoints = 100;
    }

    public Guid Id { get; set; }

    public Guid? OriginalSubjectId { get; set; }

    public string Name { get; set; }

    public User Author { get; set; }

    public int Points { get; set; }

    public int SumPoints { get; set; }

    public int MinPoints { get; set; }

    public LaboratoryWorkRepository LaboratoryWorks { get; set; }

    public LectureMaterialRepository LectureMaterials { get; set; }
}