public class SubjectTestFactory : ISubjectFactory
{
    public ISubject CreateSubject(Guid? originalSubjectId, string name, User author, int examPoints = 0, int minPoints = 0)
    {
        return new SubjectTest(originalSubjectId, name, author, minPoints);
    }
}