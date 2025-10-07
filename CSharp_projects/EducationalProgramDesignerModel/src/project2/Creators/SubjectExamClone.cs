public class SubjectExamClone
{
    public SubjectExam Copy(SubjectExam subjectExam)
    {
        return new SubjectExam(subjectExam.Id, subjectExam.ExamPoints, subjectExam.Name, subjectExam.Author);
    }
}