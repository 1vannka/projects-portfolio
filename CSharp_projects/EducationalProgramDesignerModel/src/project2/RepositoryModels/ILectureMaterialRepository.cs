public interface ILectureMaterialRepository
{
    void AddEntity(LectureMaterial entity);

    EditResult Edit(LectureMaterial entity, Guid authorid);

    LectureMaterial? GetById(Guid id);

    Guid GetAuthorId(LectureMaterial entity);
}