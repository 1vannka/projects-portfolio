public class Train : ITrain
{
    public Train(double mass, double maxForce, double precision)
    {
        Mass = mass;
        MaxForce = maxForce;
        Precision = precision;
        Speed = 0;
        Acceleration = 0;
        PassedDistance = 0;
    }

    public double Mass { get; }

    public double MaxForce { get; }

    public double Precision { get; }

    public double Speed { get; private set; }

    public double Acceleration { get; set; }

    public double PassedDistance { get; private set; }

    public void UpdateSpeed()
    {
        Speed += Acceleration * Precision;
    }

    public double CalculatePassedDistance()
    {
        PassedDistance = Speed * Precision;
        return PassedDistance;
    }
}