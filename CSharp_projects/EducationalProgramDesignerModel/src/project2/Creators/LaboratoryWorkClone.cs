public class LaboratoryWorkClone
{
    public LaboratoryWork Copy(LaboratoryWork originalLaboratoryWork)
    {
        return new LaboratoryWork(originalLaboratoryWork.Title, originalLaboratoryWork.Description, originalLaboratoryWork.GradeCriteria, originalLaboratoryWork.Points, originalLaboratoryWork.Author, originalLaboratoryWork.Id);
    }
}