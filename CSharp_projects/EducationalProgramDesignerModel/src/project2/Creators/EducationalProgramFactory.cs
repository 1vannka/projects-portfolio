public class EducationalProgramFactory
{
    public EducationalProgram Create(string title, User programManager)
    {
        return new EducationalProgram(title, programManager);
    }
}