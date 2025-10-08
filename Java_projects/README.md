This README is available in another language:  
[ru version](README.ru.md)

[Back to language list](../README.md)

# Java Projects

## Overview
This folder contains Java projects demonstrating various architectural approaches, working with databases, REST API implementation, and microservices.  
Each project illustrates the evolution of architecture: from a console application with a layered structure to full-fledged web and microservice solutions with security and integration.

---

## Projects

### 1. [ATM System](./project1/README.md)
- **Purpose:** Practice with Java, Maven, JavaDoc, and JUnit.
- **Description:** Simulates ATM operations with account creation, transactions, history, and error handling via checked exceptions.  
- **Architecture:** Layered structure (domain, service, repository, main).

---

### 2. [Database Application (JPA / Hibernate)](./project2/README.md)
- **Added:**  
  - DAO layer and ORM integration (JPA/Hibernate).
  - Bidirectional entity relationships and business logic in services.
- **Description:** Models a system for tracking pets and owners with CRUD operations, relationships, and tests.

---

### 3. [Web Application for Managing Cats and Owners (Spring Boot + REST API)](./project3/README.md)
- **Added:**  
  - Web layer with Spring Boot and Spring MVC.
  - REST API, pagination, filtering, DTO layer.
  - Documentation via Swagger.
- **Description:** Web service for managing cats and owners with modern web technologies.

---

### 4. [Web Application with Security (Spring Boot + REST API + Security)](./project4/README.md)
- **Added:**  
  - Security layer (Spring Security, authentication, authorization, roles).
  - User and session management.
  - Single entry point and access control.
- **Description:** Web application with REST API, DTO, security, and controller tests.

---

### 5. [Microservice Application (Spring Boot + REST API + RabbitMQ + Security)](./project5/README.md)
- **Added:**  
  - Microservice architecture: separate services for cats, owners, and gateway.
  - Interaction via RabbitMQ.
  - JWT authentication, routing, data isolation.
- **Description:** Microservice solution with REST API, security, integration, and tests.

---

## Common Technologies
- **Java 21**
- **Maven**
- **JavaDoc** — documentation (for console and server projects)
- **Swagger** — API documentation

---

## Summary
This folder contains projects demonstrating the evolution of architecture and technologies in Java: from simple console solutions to modern microservice systems.  
Each project is autonomous, extensible, well-tested, and illustrates specific architectural and technological approaches.


