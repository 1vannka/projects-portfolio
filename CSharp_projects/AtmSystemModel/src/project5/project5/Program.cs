string connectionString = "Host=localhost;Port=5432;Database=postgres;Username=postgres;Password=1234";
var initial = new Initial(connectionString);
initial.Initialize();

var accountRepository = new AccountRepository(connectionString);
var adminAccountRepository = new AdminAccountRepository(connectionString);
var chooseRepositoryScenario = new ChooseRepositoryScenario(accountRepository, adminAccountRepository);

chooseRepositoryScenario.Run();
