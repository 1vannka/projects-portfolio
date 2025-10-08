public class LectureMaterialFactory
{
    public LectureMaterial CreateEntity(string title, string shortDescription, string content, User author, Guid? originalLectureMaterialId)
    {
        return new LectureMaterial(title, shortDescription, content, author, originalLectureMaterialId);
    }
}