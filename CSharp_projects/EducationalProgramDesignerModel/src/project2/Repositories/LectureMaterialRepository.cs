public class LectureMaterialRepository : ILectureMaterialRepository
{
    public LectureMaterialRepository()
    {
        LectureMaterials = new List<LectureMaterial>();
    }

    private List<LectureMaterial> LectureMaterials { get; set; }

    public void AddEntity(LectureMaterial entity)
    {
        LectureMaterials.Add(entity);
    }

    public EditResult Edit(LectureMaterial entity, Guid authorid)
    {
        if (IsValidator.CanEdit<LectureMaterial>(entity, GetAuthorId(entity), entity.Id))
        {
            return new EditResult.Success();
        }

        return new EditResult.FailToEdit();
    }

    public LectureMaterial? GetById(Guid id)
    {
        foreach (LectureMaterial lectureMaterial in LectureMaterials)
        {
            if (lectureMaterial.Id == id)
            {
                return lectureMaterial;
            }
        }

        return null;
    }

    public Guid GetAuthorId(LectureMaterial entity)
    {
        User author = entity.Author;
        return author.Id;
    }
}