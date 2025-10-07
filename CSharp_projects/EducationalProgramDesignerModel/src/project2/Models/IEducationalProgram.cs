public interface IEducationalProgram
{
    public Guid Id { get; set; }

    public string Title { get; set; }

    public SubjectRepository Subjects { get; set; }

    public User ProgramManager { get; set; }
}