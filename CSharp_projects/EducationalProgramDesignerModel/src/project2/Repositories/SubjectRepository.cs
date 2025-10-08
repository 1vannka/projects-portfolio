public class SubjectRepository : ISubjectRepository
{
    public SubjectRepository()
    {
        Subjects = new List<ISubject>();
    }

    private List<ISubject> Subjects { get; set; }

    public void AddEntity(ISubject entity)
    {
        Subjects.Add(entity);
    }

    public EditResult Edit(ISubject entity, int authorid)
    {
        if (IsValidator.CanEdit<ISubject>(entity, GetAuthorId(entity), entity.Id))
        {
            return new EditResult.Success();
        }

        return new EditResult.FailToEdit();
    }

    public ISubject? GetById(Guid id)
    {
        foreach (ISubject subject in Subjects)
        {
            if (subject.Id == id)
            {
                return subject;
            }
        }

        return null;
    }

    public Guid GetAuthorId(ISubject entity)
    {
        User author = entity.Author;
        return author.Id;
    }
}
