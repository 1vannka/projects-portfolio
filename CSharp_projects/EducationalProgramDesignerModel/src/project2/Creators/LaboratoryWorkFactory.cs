public class LaboratoryWorkFactory
{
    public LaboratoryWork CreateEntity(string title, string description, string gradeCriteria, int points, User author, Guid? originalLaboratoryWorkId)
    {
        return new LaboratoryWork(title, description, gradeCriteria, points, author, originalLaboratoryWorkId);
    }
}