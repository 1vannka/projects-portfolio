public class SubjectExam : ISubject
{
    public SubjectExam(Guid? originalSubjectId, int examPoints, string name, User author)
    {
        Id = Guid.NewGuid();
        OriginalSubjectId = originalSubjectId;
        ExamPoints = examPoints;
        Name = name;
        Author = author;
        LaboratoryWorks = new LaboratoryWorkRepository();
        LectureMaterials = new LectureMaterialRepository();
        SumPoints = 100 - ExamPoints;
        Points = 0;
    }

    public Guid Id { get; set; }

    public Guid? OriginalSubjectId { get; set; }

    public int ExamPoints { get; set; }

    public string Name { get; set; }

    public User Author { get; set; }

    public int SumPoints { get; set; }

    public int Points { get; set; }

    public LaboratoryWorkRepository LaboratoryWorks { get; set; }

    public LectureMaterialRepository LectureMaterials { get; set; }
}