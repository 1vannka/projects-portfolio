public class EducationalProgram : IEducationalProgram
{
    public EducationalProgram(string title, User programManager)
    {
        Id = Guid.NewGuid();
        Title = title;
        Subjects = new SubjectRepository();
        ProgramManager = programManager;
    }

    public Guid Id { get; set; }

    public string Title { get; set; }

    public SubjectRepository Subjects { get; set; }

    public User ProgramManager { get; set; }
}