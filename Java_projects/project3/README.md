This README is available in another language:  
[ru version](README.ru.md)

[Back to project list](../README.md)

# Web Application for Managing Cats and Owners (Spring Boot + REST API)

## Purpose
Learn to work with the **Spring** framework, its modules **Spring Boot**, **Spring MVC**, and **Spring Data JPA**.  
Practice creating a **REST API**, using **Dependency Injection**, and documenting APIs with **Swagger**.

---

## Brief Description
The application is a web service providing an **HTTP interface (REST API)** for managing cat and owner entities.  
It supports **CRUD operations**, **pagination**, **filtering**, and a **DTO layer** for safe data exchange.  
Database interaction is implemented via **Spring Data JPA**.  
The project also includes **Swagger documentation** for convenient API testing.

---

## Main Features
- **REST API** for `Cat` and `Owner` entities.
- **CRUD operations**: create, read, update, and delete.
- **Pagination and filtering** (e.g., “first five ginger cats”).
- **DTO layer** — for data transfer between server and client.
- **Swagger UI** — for visual documentation and API testing.
- **Unit testing** of REST controllers using **MockMVC** without running a web server.

---

## Architecture
The application follows the **Spring MVC** pattern, with separation into layers:
- **Controller** — REST API endpoints.
- **Service** — application business logic.
- **Repository** — database access via **Spring Data JPA**.
- **DTO / Mapper** — conversion between Entity and DTO.

Dependency injection is used throughout the application.

---

## REST API Example

### Sample requests:
- `GET /api/cats` — get all cats (with pagination and filtering).
- `GET /api/cats/{id}` — get a cat by ID.
- `POST /api/cats` — add a new cat.
- `PUT /api/cats/{id}` — update cat data.
- `DELETE /api/cats/{id}` — delete a cat.

---

## Testing

### REST API is tested using:
- JUnit 5 — unit testing framework.
- MockMVC — controller testing without running a server.

## Technologies
- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- PostgreSQL
- Swagger
- JUnit 5, MockMVC
- Maven

## Result
A full-featured web application with a REST API for managing cats and their owners,
using Spring Boot, Spring Data JPA, and DTO models.
The application supports pagination, filtering, testing via MockMVC, and API documentation via Swagger.

