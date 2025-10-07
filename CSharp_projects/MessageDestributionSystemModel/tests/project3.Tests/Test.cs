public class Test
{
    [Fact]
    public void MessageForUserShouldBeUnReadFirst()
    {
        var user = new User("Ivanov");
        var userAddressee = new UserAddressee(user);
        var userProxy = new AddresseeProxy(userAddressee, PriorityLevel.Medium);
        var topic = new Topic("Test");
        var message = new Message("Test Header", "Test Body", PriorityLevel.Low);
        topic.AddAddresses(userProxy);

        topic.SendMessage(message);

        Assert.Equal("UnRead", message.ReadStatus);
    }

    [Fact]
    public void ChangeMessageStatusToReadAfterReading()
    {
        var user = new User("Ivanov");
        var userAddressee = new UserAddressee(user);
        var userProxy = new AddresseeProxy(userAddressee, PriorityLevel.Medium);
        var topic = new Topic("Test");
        var message = new Message("Test Header", "Test Body", PriorityLevel.Low);
        topic.AddAddresses(userProxy);
        topic.SendMessage(message);

        EditStatusResult result = userAddressee.ReadMessage(message);

        Assert.IsType<EditStatusResult.Success>(result);
        Assert.Equal("Read", message.ReadStatus);
    }

    [Fact]
    public void TryToReadAlreadyReadMessage_Fail()
    {
        var user = new User("Ivanov");
        var userAddressee = new UserAddressee(user);
        var userProxy = new AddresseeProxy(userAddressee, PriorityLevel.Medium);
        var topic = new Topic("Test");
        var message = new Message("Test Header", "Test Body", PriorityLevel.Low);
        topic.AddAddresses(userProxy);
        topic.SendMessage(message);

        userAddressee.ReadMessage(message);
        EditStatusResult result = userAddressee.ReadMessage(message);

        Assert.IsType<EditStatusResult.IsAlreadyRead>(result);
    }

    [Fact]
    public void UserWithLowImportanceLevelDontReceiveHighImportanceMessage()
    {
        var mockLogger = new Mock<ILogger>();
        var user = new User("Ivanov");
        var topic = new Topic("Test");
        var userAddressee = new UserAddressee(user) { Logger = mockLogger.Object };
        var userProxy = new AddresseeProxy(userAddressee, PriorityLevel.Medium);
        var message = new Message("Test Header", "Test Body", PriorityLevel.High);

        topic.AddAddresses(userProxy);
        topic.SendMessage(message);

        mockLogger.Verify(logger => logger.Log(It.Is<string>(log => log.Contains("Message is received"))), Times.Never);
        Assert.Empty(mockLogger.Invocations);
    }

    [Fact]
    public void GetMessage_Should_Log_Message_When_Received()
    {
        var mockLogger = new Mock<ILogger>();
        var user = new User("Ivanov");
        var topic = new Topic("Test");
        var userAddressee = new UserAddressee(user) { Logger = mockLogger.Object };
        var userProxy = new AddresseeProxy(userAddressee, PriorityLevel.Medium);
        var message = new Message("Test Header", "Test Body", PriorityLevel.Low);

        topic.AddAddresses(userProxy);
        topic.SendMessage(message);

        mockLogger.Verify(logger => logger.Log(It.Is<string>(log => log.Contains("Message is received"))), Times.Once);
        Assert.Single(mockLogger.Invocations);
    }

    [Fact]
    public void MessengerAdapterShouldSendMessageToMessenger()
    {
        var mockMessenger = new Mock<IMessenger>();
        var messengerAdapter = new MessengerAdapter(mockMessenger.Object);
        var messengerProxy = new AddresseeProxy(messengerAdapter, PriorityLevel.Medium);
        var topic = new Topic("Test");
        var message = new Message("Test Header", "Test Body", PriorityLevel.Medium);
        topic.AddAddresses(messengerProxy);
        topic.SendMessage(message);

        mockMessenger.Verify(m => m.SendMessage(It.Is<string>(text => text.Contains("Test Header: Test Body"))), Times.Once);
    }

    [Fact]
    public void User_With_Importance_Filter_Should_Receive_Message_Once()
    {
        var mockLogger = new Mock<ILogger>();
        var user = new User("Ivanov");
        var topic = new Topic("Test");
        var userWithFilter = new UserAddressee(user) { Logger = mockLogger.Object };
        var userWithoutFilter = new UserAddressee(user) { Logger = mockLogger.Object };
        var userProxyWithFilter = new AddresseeProxy(userWithFilter, PriorityLevel.Low);
        var userProxyWithoutFilter = new AddresseeProxy(userWithoutFilter, null);
        var message = new Message("Test Header", "Test Body", PriorityLevel.Medium);

        topic.AddAddresses(userProxyWithFilter);
        topic.AddAddresses(userProxyWithoutFilter);
        topic.SendMessage(message);

        mockLogger.Verify(logger => logger.Log(It.Is<string>(log => log.Contains("Message is received"))), Times.Once);
        Assert.Single(mockLogger.Invocations);
    }
}