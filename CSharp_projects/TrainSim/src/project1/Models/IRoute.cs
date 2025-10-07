public interface IRoute
{
    public double TotalTime { get; set; }

    public double SpeedLimit { get; }

    public PassRouteResult SimulateRoutePassage(ITrain train);
}