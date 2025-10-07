public interface IValidator
{
   static abstract bool CanEdit<T>(T entity, Guid authorid, Guid id) where T : class;
}