public class Station : IRailPart
{
    private double Congestionfactor { get; }

    private double MaxSpeed { get; }

    public Station(double congestionFactor, double maxspeed)
    {
        Congestionfactor = congestionFactor;
        Time = 0;
        MaxSpeed = maxspeed;
    }

    public double Time { get; set; }

    public double TimeToPass { get; protected set; }

    public bool TryToPass(ITrain train)
    {
        if (train.Speed > MaxSpeed)
        {
            return false;
        }

        return true;
    }

    public void Stop(ITrain train)
    {
        train.Acceleration = 0;
    }

    public double CalculateTime(ITrain train)
    {
        if (Congestionfactor != 0)
        {
            Time += Congestionfactor * 2;
        }

        Stop(train);
        return Time;
    }
}