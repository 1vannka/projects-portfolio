public interface IRailPart
{
    bool TryToPass(ITrain train);

    double CalculateTime(ITrain train);
}