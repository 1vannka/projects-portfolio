public class SubjectTestClone
{
    public SubjectTest Copy(SubjectTest subjectTest)
    {
        return new SubjectTest(subjectTest.Id, subjectTest.Name, subjectTest.Author, subjectTest.MinPoints);
    }
}