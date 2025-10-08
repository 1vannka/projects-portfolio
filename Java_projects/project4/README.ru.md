Этот README доступен на другом языке:  
[en version](README.md)

[Вернуться к списку проектов](../README.ru.md)

# Веб-приложение для работы с котами и их владельцами (Spring Boot + REST API + Security)

## Цель
Освоить работу с фреймворком **Spring Boot**, реализацию REST API, внедрение слоёв безопасности, работу с JPA и тестирование.

---

## Краткое описание
Приложение предоставляет HTTP-интерфейс (REST API) для управления сущностями котиков и их владельцев.  
Реализованы CRUD-операции, DTO-слой, аутентификация и авторизация пользователей, а также тестирование контроллеров.

---

## Основные функции
- REST API для сущностей `Cat`, `Owner`, `User`.
- CRUD-операции для котиков и владельцев.
- DTO-слой для безопасной передачи данных (`CatDto`, `OwnerDto`, `UserDto`, `RegisterDto`).
- Аутентификация и авторизация через Spring Security.
- Роли пользователей (`Role`), с разграничением доступа.
- Unit-тесты контроллеров через MockMVC.

---

## Архитектура
Многослойная структура:
- **Controller** (`controllers`) — REST endpoints для котиков, владельцев, пользователей, сессий и аутентификации.
- **Service** (`services`) — бизнес-логика, управление сущностями, обработка пользователей.
- **Repository** (`repositories`) — доступ к данным через Spring Data JPA.
- **DTO / Mapper** (`Dto`) — преобразование между Entity и DTO.
- **Models** (`models`) — бизнес-объекты: `Cats`, `Owners`, `Users`, `Role`, перечисление `Color`.
- **Конфигурация** (`config/SecurityConfig.java`) — настройка Spring Security.

Внедрение зависимостей осущес��вляется через Dependency Injection.

---

## Пример REST API

### Примеры запросов:
- `GET /api/cats` — получить всех котиков.
- `GET /api/owners` — получить всех владельцев.
- `POST /api/auth/register` — регистрация пользователя.
- `POST /api/auth/login` — вход пользователя.
- `GET /api/session` — информация о текущей сессии.

---

## Тестирование

- JUnit 5 — модульные тесты.
- MockMVC — тестирование контроллеров.

---

## Технологии
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

## Конфигурация
- Основные настройки приложения — `resources/application.yml`.
- Конфигурация JPA/Hibernate — `resources/META-INF/persistence.xml`.

---

## Результат
Веб-приложение с REST API для управления котиками, владельцами и пользователями, с поддержкой аутентификации, авторизации, DTO-слоя и тестирования.

---

