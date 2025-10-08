This README is available in other languages:
[🇷🇺 version](README.ru.md)

[Back to project list](../README.md)


# ATM System

## Purpose
To demonstrate understanding of **multilayered architectures** and **design patterns** by implementing an ATM system.  
The system separates business logic from persistence and presentation layers, following a onion-layered architecture.

---

## Overview
The application simulates the operations of an ATM, allowing users and administrators to interact via a console interface.  
It supports account creation, balance checking, cash withdrawal and deposit, and transaction history viewing.  
Data is persistently stored in **PostgreSQL**, without using any ORM libraries.

---

## Core Features
- **Account Management**: create accounts and view balances.  
- **Transactions**: withdraw and deposit funds with balance validation.  
- **History**: view transaction history for each account.  
- **Console Interface**: interactive command-based interface.  
- **Modes**: 
  - **User mode** — requires account number and PIN.  
  - **Administrator mode** — requires system password (configurable).  
- **Error Handling**: invalid operations or credentials produce informative messages.  
- **Persistence**: account and transaction data stored in PostgreSQL.  
- **Architecture**:  onion-layered domain model.

---

## Test Scenarios

| # | Operation | Test Case | Expected Result |
|---|-----------|-----------|----------------|
| 1 | Withdraw | Sufficient balance | Account balance updated correctly |
| 2 | Withdraw | Insufficient balance | Service returns error |
| 3 | Deposit | Any amount | Account balance updated correctly |

---

## Technologies
- **C# (.NET 8)**
- **PostgreSQL** — persistent storage  
- **xUnit** — unit testing  
- **Patterns Used**: hexagonal architecture, layered design, repository pattern, mocks

---

## Result
A functional ATM system with persistent storage, interactive console interface, and separation of concerns between business logic, persistence, and presentation.  
Business logic is fully testable with mocked repositories, ensuring reliability and maintainability.
