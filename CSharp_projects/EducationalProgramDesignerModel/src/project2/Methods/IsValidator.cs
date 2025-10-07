public class IsValidator : IValidator
{
    public static bool CanEdit<T>(T entity, Guid authorid, Guid id) where T : class
    {
        if (authorid == id)
            return true;

        return false;
    }

    public CreateEntityResult IsPossibleToCreate(ISubject subject, LaboratoryWorkRepository laboratoryWorkRepository)
    {
        if (subject.SumPoints != laboratoryWorkRepository.TotalPoints)
        {
            return new CreateEntityResult.FailToCreate();
        }

        return new CreateEntityResult.Success();
    }
}