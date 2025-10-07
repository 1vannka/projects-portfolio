public class PowerMagneticPart : IRailPart
{
    public PowerMagneticPart(double length, double force)
    {
        Length = length;
        Time = 0;
        CurrentAcceleration = 0;
        Force = force;
        RemainingDistance = length;
    }

    public double Length { get; }

    public double Force { get; }

    public double Time { get; private set; }

    public double CurrentAcceleration { get; private set; }

    public double RemainingDistance { get; private set; }

    public void ChangeAcceleration(ITrain train)
    {
        train.Acceleration += Force / train.Mass;
    }

    public bool TryToPass(ITrain train)
    {
        if (Force > train.MaxForce || Length < 0)
        {
            return false;
        }

        while (RemainingDistance > 0)
        {
            ChangeAcceleration(train);
            train.UpdateSpeed();
            if (train.Speed < 0)
            {
                return false;
            }

            if (train.Speed == 0 && train.Acceleration == 0)
            {
                return false;
            }

            RemainingDistance -= train.CalculatePassedDistance();
        }

        return true;
    }

    public double CalculateTime(ITrain train)
    {
        if (Length >= 0)
        {
            Time = Length / train.Speed;
        }

        return Time;
    }
}