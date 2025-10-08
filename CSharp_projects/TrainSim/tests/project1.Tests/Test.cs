using Xunit;

namespace project1.Tests;

public class Test
{
    [Fact]
    public void SuccessfulAccelerationtoRouteSpeed_Success()
    {
        var train = new Train(1000, 500, 1);
        var path = new Route(
            new List<IRailPart> { new PowerMagneticPart(600, 200), new RegularMagneticPart(200), },
            150);

        PassRouteResult passResult = path.SimulateRoutePassage(train);

        Assert.IsType<PassRouteResult.Success>(passResult);
    }

    [Fact]
    public void ExcessiveAcceleration_Failure()
    {
        var train = new Train(200, 10500, 100);
        var path = new Route(
                new List<IRailPart> { new PowerMagneticPart(100, 10000), new RegularMagneticPart(100), },
                150);

        PassRouteResult passResult = path.SimulateRoutePassage(train);

        Assert.IsType<PassRouteResult.FailToStop>(passResult);
    }

    [Fact]
    public void StationAwareAcceleration_Success()
    {
        var train = new Train(200, 2000, 100);
        var path = new Route(
            new List<IRailPart> { new PowerMagneticPart(100, 500), new RegularMagneticPart(100), new Station(10, 700), new RegularMagneticPart(100) },
            700);

        PassRouteResult passResult = path.SimulateRoutePassage(train);

        Assert.IsType<PassRouteResult.Success>(passResult);
    }

    [Fact]
    public void ExcessiveStationSpeed_Failure()
    {
        var train = new Train(200, 2000, 100);
        var path = new Route(new List<IRailPart> { new PowerMagneticPart(100, 1000), new Station(10, 450), new RegularMagneticPart(100) }, 150);

        PassRouteResult passResult = path.SimulateRoutePassage(train);

        Assert.IsType<PassRouteResult.FailToPassRoute>(passResult);
    }

    [Fact]
    public void RouteSpeedExceededStation_Failure()
    {
        var train = new Train(200, 2000, 100);
        var path = new Route(
            new List<IRailPart> { new PowerMagneticPart(100, 350), new RegularMagneticPart(100), new Station(10, 450), new RegularMagneticPart(100) },
            300);

        PassRouteResult passResult = path.SimulateRoutePassage(train);

        Assert.IsType<PassRouteResult.FailToStop>(passResult);
    }

    [Fact]
    public void ComplexSpeedManagement_Success()
    {
        var train = new Train(350, 2000, 100);
        var path = new Route(
            new List<IRailPart> { new PowerMagneticPart(100, 450), new RegularMagneticPart(100), new PowerMagneticPart(100, -1000), new Station(10, 450), new RegularMagneticPart(100), new PowerMagneticPart(100, 350), new RegularMagneticPart(100), new PowerMagneticPart(100, -1000) },
            125);

        PassRouteResult passResult = path.SimulateRoutePassage(train);

        Assert.IsType<PassRouteResult.Success>(passResult);
    }

    [Fact]
    public void NoAcceleration_Failure()
    {
        var train = new Train(200, 2000, 100);
        var path = new Route(
            new List<IRailPart> { new RegularMagneticPart(100) }, 700);

        PassRouteResult passResult = path.SimulateRoutePassage(train);

        Assert.IsType<PassRouteResult.FailToPassRoute>(passResult);
    }

    [Fact]
    public void ForceCancellation_Failure()
    {
        var train = new Train(200, 2000, 100);
        var path = new Route(
            new List<IRailPart> { new PowerMagneticPart(100, 500), new PowerMagneticPart(100, -1000) },
            700);

        PassRouteResult passResult = path.SimulateRoutePassage(train);

        Assert.IsType<PassRouteResult.FailToPassRoute>(passResult);
    }
}




