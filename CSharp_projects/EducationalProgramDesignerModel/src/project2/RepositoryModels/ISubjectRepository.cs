public interface ISubjectRepository
{
    void AddEntity(ISubject entity);

    EditResult Edit(ISubject entity, int authorid);

    ISubject? GetById(Guid id);

    Guid GetAuthorId(ISubject entity);
}