This README is available in another language:  
[ru version](README.ru.md)

[Back to project list](../README.md)

# ATM System

## Purpose
Practice working with **Java**, build systems (**Maven / Gradle**), **JavaDoc** documentation generation, and unit testing with **JUnit**.  
Implement a functional ATM model with checked business logic exceptions.

---

## Brief Description
The application simulates an ATM, allowing users to create accounts, check balances, withdraw and deposit funds, and view transaction history.  
The project is built using Maven or Gradle, automatically generates **JavaDoc**, and includes a separate script for launching the interactive console interface.

---

## Main Features
- **Account creation** — register a new account.
- **Balance inquiry** — get the current balance.
- **Withdrawal** — decrease the balance if sufficient funds are available.
- **Deposit** — increase the balance.
- **Transaction history** — view all transactions.
- **Error handling** — checked exceptions are thrown for invalid operations.
- **Console interface** — separate interactive module for user interaction.
- **JavaDoc documentation** — automatically generated during project build.
- **Unit tests** — cover business logic, including exception scenarios.

---

## Test Scenarios

| # | Operation | Test Case | Expected Result |
|---|-----------|-----------|----------------|
| 1 | Withdrawal | Sufficient balance | Account balance updated correctly |
| 2 | Withdrawal | Insufficient balance | `InsufficientFundsException` is thrown |
| 3 | Deposit | Any amount | Account balance updated correctly |
| 4 | PIN check | Incorrect PIN | `InvalidPinException` is thrown |
| 5 | View history | After several transactions | Correct list of operations is displayed |

---

## Technologies
- **Java 21**
- **Maven** — build and dependency management
- **JUnit 5** — unit tests
- **JavaDoc** — automatic documentation generation
- **No Spring / Spring Boot**

---

## Implementation

- The application is built with a **multi-layered structure**:  
  - **domain** — contains main entities (e.g., Account, Transaction).
  - **service** — implements ATM business logic (e.g., transaction processing, PIN verification).
  - **repository** — responsible for data storage and access (e.g., accounts and transaction history).
  - **main** — contains the console interface for user interaction.
- All business logic is separated from the console interface and implemented in the service layer.
- Business logic exceptions are implemented as **checked exceptions** (e.g., InsufficientFundsException, InvalidPinException).
- Public classes and methods are documented using **JavaDoc**.
- The build pipeline includes:
    - automatic JavaDoc generation,
    - a separate step for launching the console interface.

---

## Result
A fully implemented ATM application with separated logic, documented API, JavaDoc autogeneration, and unit tests for business logic.  
The system is easily extensible, testable, and does not depend on a specific input/output interface implementation.


