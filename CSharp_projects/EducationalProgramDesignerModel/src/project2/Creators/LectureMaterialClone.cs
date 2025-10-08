public class LectureMaterialClone
{
    public LectureMaterial Copy(LectureMaterial originalLectureMaterial)
    {
        return new LectureMaterial(originalLectureMaterial.Title, originalLectureMaterial.ShortDescription, originalLectureMaterial.Content, originalLectureMaterial.Author, originalLectureMaterial.Id);
    }
}