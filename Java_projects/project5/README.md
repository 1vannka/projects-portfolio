This README is available in another language:  
[ru version](README.ru.md)

[Back to project list](../README.md)

# Microservice Application for Managing Cats and Owners (Spring Boot + REST API + RabbitMQ + Security)

## Purpose
Learn to build microservice architecture with Spring Boot, integrate via RabbitMQ, implement REST API, add security, and work with DTO.

---

## Brief Description
The application consists of three microservices:  
- **cat-service** — manages cats  
- **owner-service** — manages owners  
- **gateway** — single entry point, request routing, authentication and authorization

Microservices interact via RabbitMQ. CRUD operations, DTO layer, authentication, authorization, and testing are implemented.

---

## Main Features
- REST API for `Cat`, `Owner`, `User` entities
- CRUD operations for cats and owners
- DTO layer for data transfer (`CatDto`, `OwnerDto`, `UserDto`, `RegisterDto`)
- Authentication and authorization via JWT (Spring Security)
- User roles (`Role`)
- Service interaction via RabbitMQ
- Swagger documentation (if enabled)
- Unit tests for controllers using MockMVC

---

## Architecture

Microservice structure:
- **cat-service**  
  - Cat management  
  - Service, repository, DTO layers  
  - RabbitMQ listener for message processing  
- **owner-service**  
  - Owner management  
  - Service, repository, DTO layers  
  - RabbitMQ listener  
- **gateway**  
  - REST controllers (`CatController`, `OwnerController`, `AuthController`, `SessionController`)  
  - Service, repository, DTO, Security layers  
  - JWT authentication and authorization  
  - Routing and interaction with other services via RabbitMQ

Dependency injection is used throughout the application.

---

## REST API Example

### Sample requests:
- `GET /api/cats` — get all cats
- `GET /api/owners` — get all owners
- `POST /api/auth/register` — register a user
- `POST /api/auth/login` — user login
- `GET /api/session` — get current session info

---

## Testing

- JUnit 5 — unit tests
- MockMVC — controller testing (`gateway/src/test/java/com/project5`)

---

## Technologies
- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security (JWT)
- RabbitMQ
- PostgreSQL
- Swagger
- JUnit 5, MockMVC
- Maven

---

## Configuration
- Main microservice settings — `cat-service/resources/application.yml`, `owner-service/resources/application.yml`, `gateway/resources/application.yml`
- RabbitMQ configuration — `RabbitMQConfig.java` in each service

---

## Features and Differences from Previous Project

- Microservice architecture: three separate services instead of a monolith
- Service interaction via RabbitMQ
- Gateway service implements routing, security, and a single entry point
- Stricter isolation of business logic and data
- JWT for authorization
- Each service has its own configuration and DTO layer

---

## Result
A microservice application with REST API for managing cats, owners, and users, supporting authentication, authorization, DTO layer, RabbitMQ, and testing.

---

