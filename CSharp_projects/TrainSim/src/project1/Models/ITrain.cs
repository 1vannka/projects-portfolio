public interface ITrain
{
    public double Mass { get; }

    public double MaxForce { get; }

    public double Precision { get; }

    public double Speed { get; }

    public double Acceleration { get; set; }

    public double PassedDistance { get; }

    public void UpdateSpeed();

    public double CalculatePassedDistance();
}