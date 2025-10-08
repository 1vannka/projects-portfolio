public class RegularMagneticPart : IRailPart
{
    public RegularMagneticPart(double length)
    {
        Length = length;
        Time = 0;
    }

    public double Length { get; }

    public double Time { get; protected set; }

    public bool TryToPass(ITrain train)
    {
        if (train.Speed <= 0 || Length < 0)
        {
            return false;
        }

        return true;
    }

    public double CalculateTime(ITrain train)
    {
        if (Length >= 0)
        {
            Time = Length / train.Speed;
        }

        train.UpdateSpeed();

        return Time;
    }
}