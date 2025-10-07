public class Test
{
    [Fact]
    public void AccountBalanceSaveCorrectlyAfterRefill()
    {
        var account = new Account(100, "Aboba");
        var mockAccountRepository = new Mock<IAccountRepository>();
        var accountService = new AccountService(mockAccountRepository.Object);
        accountService.Account = account;
        mockAccountRepository.Setup(repo => repo.FindAccountByNumber(account.Number)).Returns(account);
        accountService.Refill(100);
        mockAccountRepository.Verify(repo => repo.UpdateAccount(It.Is<Account>(a => a.Balance == 100)), Times.Once);
        }

    [Fact]
    public void AccountBalanceSaveCorrectlyAfterWithdraw()
    {
            var account = new Account(100, "Aboba");
            account.Balance = 1000;
            var mockAccountRepository = new Mock<IAccountRepository>();
            var accountService = new AccountService(mockAccountRepository.Object);
            accountService.Account = account;
            mockAccountRepository.Setup(repo => repo.FindAccountByNumber(account.Number)).Returns(account);
            accountService.Withdraw(100);
            mockAccountRepository.Verify(repo => repo.UpdateAccount(It.Is<Account>(a => a.Balance == 900)), Times.Once);
            }

    [Fact]
    public void FailureWithdrawDueToInsufficientFunds()
    {
           var account = new Account(100, "Aboba");
           account.Balance = 1000;
           var mockAccountRepository = new Mock<IAccountRepository>();
           var accountService = new AccountService(mockAccountRepository.Object);
           accountService.Account = account;
           mockAccountRepository.Setup(repo => repo.FindAccountByNumber(account.Number)).Returns(account);
           accountService.Withdraw(10000);
           mockAccountRepository.Verify(repo => repo.UpdateAccount(It.Is<Account>(a => a.Balance == 900)), Times.Never);
    }
}