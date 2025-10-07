public class Route : IRoute
{
    public Route(IReadOnlyCollection<IRailPart> parts, double speedlimit)
    {
        _parts = new List<IRailPart>(parts);
        SpeedLimit = speedlimit;
        TotalTime = 0;
    }

    private readonly List<IRailPart> _parts;

    public double TotalTime { get; set; }

    public double SpeedLimit { get; }

    public PassRouteResult SimulateRoutePassage(ITrain train)
    {
        foreach (IRailPart part in _parts)
        {
            if (part.TryToPass(train))
            {
                TotalTime += part.CalculateTime(train);
            }
            else
            {
                return new PassRouteResult.FailToPassRoute();
            }
        }

        if (SpeedLimit > train.Speed)
        {
            return new PassRouteResult.Success();
        }
        else
        {
            return new PassRouteResult.FailToStop();
        }
    }
}