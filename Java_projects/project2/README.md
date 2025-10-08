This README is available in another language:  
[ru version](README.ru.md)

[Back to project list](../README.md)

# Pet Database Application (JPA / Hibernate)

## Purpose
Learn the **Controller–Service–DAO** architectural approach, as well as working with **JDBC** and **ORM** (via **Hibernate**).  
Practice designing application structure and working with persistent storage.

---

## Brief Description
The application models a system for tracking pets and their owners.  
Each pet has a name, birth date, breed, color, and a list of friends (other cats).  
Owners can have multiple pets.  
CRUD operations for all entities are implemented using **JPA** and **Hibernate**.  
Data access is handled via DAO, business logic is in services, and management is through controllers.

---

## Main Entities

### Cat (Pet)
- Name
- Birth date
- Breed
- Color (enum)
- Owner
- List of friends (other cats)

### Owner
- Name
- Birth date
- List of pets

---

## Main Features
- **Create**, **view**, **edit**, **delete** pets and owners.
- **Bidirectional relationship** between pet and owner.
- **Saving friendship links** between pets.
- **Database migrations** support.
- **Logic encapsulation** in layers:
    - **DAO** — direct database interaction via Hibernate / JDBC.
    - **Service** — business logic and exception handling.
    - **Controller** — user interface (e.g., console).

---

## Implementation

- The project uses a multi-layered architecture:
    - **models** — business objects: Cat, Owner entities, Color enum.
    - **repositories** — data access layer: IRepository interface, CatRepository and OwnerRepository implementations using JPA/Hibernate.
    - **services** — business logic: CatService and OwnerService classes manage entity operations and exception handling.
    - **scenarios** — workflow scenarios: CatWorkScenario and OwnerWorkScenario classes implement user scenarios.
    - **app** — entry point: Main class implements the console interface and interacts with services.
- CRUD operations are implemented for each entity.
- Relationships between pets and owners, as well as pet friendships, are implemented via JPA associations.
- Business logic exceptions are implemented as **checked exceptions**.
- Public classes and methods are documented with **JavaDoc**.
- Unit tests for business logic and repositories are located in test/java/com/project2/tests.
- JPA/Hibernate configuration is stored in resources/META-INF/persistence.xml.
- The application is built and run using Maven.

---

## Technologies
- **Java 21**
- **Maven** — build and dependency management
- **JPA / Hibernate** — ORM for database interaction
- **H2 Database** — embedded database for development and testing
- **JUnit 5** — unit tests
- **JavaDoc** — automatic documentation generation
- **No Spring / Spring Boot**

---

## Result
A functional CRUD application with layered logic separation, working with PostgreSQL via Hibernate,
isolated unit tests, and extensibility for future development.
