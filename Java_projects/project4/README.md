This README is available in another language:  
[ru version](README.ru.md)

[Back to project list](../README.md)

# Web Application for Managing Cats and Owners (Spring Boot + REST API + Security)

## Purpose
Learn to work with the **Spring Boot** framework, implement REST API, add security layers, work with JPA, and perform testing.

---

## Brief Description
The application provides an HTTP interface (REST API) for managing cat and owner entities.  
CRUD operations, a DTO layer, user authentication and authorization, and controller testing are implemented.

---

## Main Features
- REST API for `Cat`, `Owner`, and `User` entities.
- CRUD operations for cats and owners.
- DTO layer for safe data transfer (`CatDto`, `OwnerDto`, `UserDto`, `RegisterDto`).
- Authentication and authorization via Spring Security.
- User roles (`Role`) with access control.
- Swagger documentation (if enabled).
- Unit tests for controllers using MockMVC.

---

## Architecture
Multi-layered structure:
- **Controller** (`controllers`) — REST endpoints for cats, owners, users, sessions, and authentication.
- **Service** (`services`) — business logic, entity management, user handling.
- **Repository** (`repositories`) — data access via Spring Data JPA.
- **DTO / Mapper** (`Dto`) — conversion between Entity and DTO.
- **Models** (`models`) — business objects: `Cats`, `Owners`, `Users`, `Role`, `Color` enum.
- **Configuration** (`config/SecurityConfig.java`) — Spring Security setup.

Dependency injection is used throughout the application.

---

## REST API Example

### Sample requests:
- `GET /api/cats` — get all cats.
- `GET /api/owners` — get all owners.
- `POST /api/auth/register` — register a user.
- `POST /api/auth/login` — user login.
- `GET /api/session` — get current session info.

---

## Testing

- JUnit 5 — unit tests.
- MockMVC — controller testing.

---

## Technologies
- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security
- PostgreSQL
- Swagger
- JUnit 5, MockMVC
- Maven

---

## Configuration
- Main application settings — `resources/application.yml`.
- JPA/Hibernate configuration — `resources/META-INF/persistence.xml`.

---

## Result
A web application with a REST API for managing cats, owners, and users, supporting authentication, authorization, DTO layer, and testing.

---

