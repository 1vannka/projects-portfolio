public interface ISubjectFactory
{
    ISubject CreateSubject(Guid? originalSubjectId, string name, User author, int examPoints = 0, int minPoints = 0);
}