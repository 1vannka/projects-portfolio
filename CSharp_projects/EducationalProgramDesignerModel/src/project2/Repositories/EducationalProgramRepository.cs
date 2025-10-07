public class EducationalProgramRepository : IEducationalProgramRepository
{
    public EducationalProgramRepository()
    {
        EducationalPrograms = new Dictionary<int, EducationalProgram>();
    }

    private Dictionary<int, EducationalProgram> EducationalPrograms { get; set; }

    public void AddEntity(EducationalProgram entity, int semester)
    {
        EducationalPrograms.Add(semester, entity);
    }

    public EducationalProgram? GetById(Guid id)
    {
        foreach (EducationalProgram educationalProgram in EducationalPrograms.Values)
        {
            if (educationalProgram.Id == id)
            {
                return educationalProgram;
            }
        }

        return null;
    }

    public Guid GetProgramManagerId(EducationalProgram entity)
    {
        User programManager = entity.ProgramManager;
        return programManager.Id;
    }
}