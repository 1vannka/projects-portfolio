using Xunit;

namespace project2.Tests;

public class Test
{
    [Fact]
    public void TryEditLaboratoryWorkByNotAuthor_Fail()
    {
        var user1 = new User("Ivanov");
        var user2 = new User("Sergeev");
        var laboratoryWorkRepository = new LaboratoryWorkRepository();
        var laboratoryWorkFactory = new LaboratoryWorkFactory();
        LaboratoryWork laboratoryWork = laboratoryWorkFactory.CreateEntity("LW1", "Trains", "Need to be done", 20, user1, null);
        laboratoryWorkRepository.AddEntity(laboratoryWork);

        EditResult editResult = laboratoryWorkRepository.Edit(laboratoryWork, user2.Id);

        Assert.IsType<EditResult.FailToEdit>(editResult);
    }

    [Fact]
    public void CopyLaboratoryWorkShouldContainOriginalId_Success()
    {
        var user1 = new User("Ivanov");
        var laboratoryWorkRepository = new LaboratoryWorkRepository();
        var laboratoryWorkFactory = new LaboratoryWorkFactory();
        LaboratoryWork laboratoryWork = laboratoryWorkFactory.CreateEntity("LW1", "Trains", "Need to be done", 20, user1, null);
        laboratoryWorkRepository.AddEntity(laboratoryWork);
        var laboratoryWorkClone = new LaboratoryWorkClone();
        LaboratoryWork laboratoryWorkCopy = laboratoryWorkClone.Copy(laboratoryWork);
        laboratoryWorkRepository.AddEntity(laboratoryWorkCopy);

        Guid originalId = laboratoryWork.Id;
        Guid? copyId = laboratoryWorkCopy.OriginalLaboratoryWorkId;

        Assert.Equal(originalId, copyId);
    }

    [Fact]
    public void CopyLectureMaterialShouldContainOriginalId_Success()
    {
        var user1 = new User("Ivanov");
        var lectureMaterialRepository = new LectureMaterialRepository();
        var lectureMaterialFactory = new LectureMaterialFactory();
        LectureMaterial lectureMaterial = lectureMaterialFactory.CreateEntity("LM1", "LabDescription", "LectureMaterial", user1, null);
        lectureMaterialRepository.AddEntity(lectureMaterial);
        var lectureMaterialClone = new LectureMaterialClone();
        LectureMaterial lectureMaterialCopy = lectureMaterialClone.Copy(lectureMaterial);
        lectureMaterialRepository.AddEntity(lectureMaterialCopy);

        Guid originalId = lectureMaterial.Id;
        Guid? copyId = lectureMaterialCopy.OriginalLectureMaterialId;

        Assert.Equal(originalId, copyId);
    }

    [Fact]
    public void CopySubjectShouldContainOriginalId_Success()
    {
        var user1 = new User("Ivanov");
        var subjectRepository = new SubjectRepository();
        var examFactory = new SubjectExamFactory();
        ISubject subject = examFactory.CreateSubject(null, "Maths", user1, 20);
        subjectRepository.AddEntity(subject);
        var subjectExamClone = new SubjectExamClone();
        ISubject subjectCopy = subjectExamClone.Copy((SubjectExam)subject);
        subjectRepository.AddEntity(subjectCopy);

        Guid originalId = subject.Id;
        Guid? copyId = subjectCopy.OriginalSubjectId;

        Assert.Equal(originalId, copyId);
    }

    [Fact]
    public void CopySubject2ShouldContainOriginalId_Success()
    {
        var user1 = new User("Ivanov");
        var subjectRepository = new SubjectRepository();
        var testFactory = new SubjectTestFactory();
        ISubject subject = testFactory.CreateSubject(null, "Maths", user1, 0, 60);
        subjectRepository.AddEntity(subject);
        var subjectTestClone = new SubjectTestClone();
        ISubject subjectCopy = subjectTestClone.Copy((SubjectTest)subject);
        subjectRepository.AddEntity(subjectCopy);

        Guid originalId = subject.Id;
        Guid? copyId = subjectCopy.OriginalSubjectId;

        Assert.Equal(originalId, copyId);
    }

    [Fact]
    public void CreateSubjectWithInvalidPoints_Failure()
    {
        var user1 = new User("Ivanov");
        var subjectRepository = new SubjectRepository();
        var examFactory = new SubjectExamFactory();
        ISubject subject = examFactory.CreateSubject(null, "Maths", user1, 20);
        subjectRepository.AddEntity(subject);
        var laboratoryWorkRepository = new LaboratoryWorkRepository();
        var laboratoryWorkFactory = new LaboratoryWorkFactory();
        LaboratoryWork firstLaboratoryWork = laboratoryWorkFactory.CreateEntity("LW1", "Trains", "Need to be done", 30, user1, null);
        laboratoryWorkRepository.AddEntity(firstLaboratoryWork);
        LaboratoryWork secondLaboratoryWork = laboratoryWorkFactory.CreateEntity("LW2", "AlsoTrains", "Need to be done", 30, user1, null);
        laboratoryWorkRepository.AddEntity(secondLaboratoryWork);
        subject.LaboratoryWorks = laboratoryWorkRepository;
        var validator = new IsValidator();

        CreateEntityResult createEntityResult = validator.IsPossibleToCreate(subject, subject.LaboratoryWorks);

        Assert.IsType<CreateEntityResult.FailToCreate>(createEntityResult);
    }
}