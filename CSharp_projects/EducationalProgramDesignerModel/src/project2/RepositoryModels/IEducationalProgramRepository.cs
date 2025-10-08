public interface IEducationalProgramRepository
{
    void AddEntity(EducationalProgram entity, int semester);

    EducationalProgram? GetById(Guid id);

    Guid GetProgramManagerId(EducationalProgram entity);
}