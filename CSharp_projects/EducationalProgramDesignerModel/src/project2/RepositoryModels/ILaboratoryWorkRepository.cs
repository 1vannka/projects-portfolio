public interface ILaboratoryWorkRepository
{
    void AddEntity(LaboratoryWork entity);

    EditResult Edit(LaboratoryWork entity, Guid id);

    LaboratoryWork? GetById(Guid id);

    Guid GetAuthorId(LaboratoryWork entity);
}