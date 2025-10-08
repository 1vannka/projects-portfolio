public class SubjectExamFactory : ISubjectFactory
{
    public ISubject CreateSubject(Guid? originalSubjectId, string name, User author, int examPoints = 0, int minPoints = 0)
    {
        return new SubjectExam(originalSubjectId, examPoints, name, author);
    }
}
