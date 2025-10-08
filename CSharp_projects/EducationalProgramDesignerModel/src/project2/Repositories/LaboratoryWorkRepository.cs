public class LaboratoryWorkRepository : ILaboratoryWorkRepository
{
    public LaboratoryWorkRepository()
    {
       LaboratoryWorks = new List<LaboratoryWork>();
       TotalPoints = 0;
    }

    public int TotalPoints { get; set; }

    private List<LaboratoryWork> LaboratoryWorks { get; set; }

    public void AddEntity(LaboratoryWork entity)
    {
        LaboratoryWorks.Add(entity);
        TotalPoints += entity.Points;
    }

    public EditResult Edit(LaboratoryWork entity, Guid id)
    {
        if (IsValidator.CanEdit<LaboratoryWork>(entity, GetAuthorId(entity), id))
        {
            return new EditResult.Success();
        }

        return new EditResult.FailToEdit();
    }

    public LaboratoryWork? GetById(Guid id)
    {
        foreach (LaboratoryWork laboratoryWork in LaboratoryWorks)
        {
            if (laboratoryWork.Id == id)
            {
                return laboratoryWork;
            }
        }

        return null;
    }

    public Guid GetAuthorId(LaboratoryWork entity)
    {
        User author = entity.Author;
        return author.Id;
    }
}