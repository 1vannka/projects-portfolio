This README is available in other languages:
[🇷🇺 version](README.ru.md)

# Educational Program Constructor

## Purpose
To reinforce the principles of **OOP**, **SOLID**, and **GRASP**, and implement **creational design patterns** through a system for constructing educational programs.  
All key model components are covered by unit tests.

---

## Overview
The system simulates the process of designing university educational programs.  
Users can create lab works, lectures, subjects, and programs, as well as clone existing entities while preserving a reference to the original.  
Editing is restricted to authors only — if business rules are violated, the system returns appropriate errors.

---

## Main Components
- **User** — the author of created entities.  
- **LabWork** — a lab assignment with a description, evaluation criteria, and score; can inherit from another lab.  
- **LectureMaterial** — a lecture with a short description and content; supports cloning.  
- **Subject** — a course that includes lab works and lectures.  
  - Has an assessment format: **exam** (with total score) or **pass/fail** (with minimum passing score).  
  - The total score for a subject must always be **100**.  
- **EducationalProgram** — a program combining multiple subjects across semesters, managed by a supervisor.

---

## Applied Patterns
- **Prototype** — for creating new entities based on existing ones.  
- **Builder** — for assembling subjects and programs.  
- **Factory Method / Abstract Factory** — for creating subjects with different assessment formats.  
- **Repository (In-Memory)** — for storing and retrieving entities by ID.  
- **AuthorProvider** — for automatically assigning authors to newly created objects.

---

## Test Scenarios

| # | Test Case | Expected Result |
|---|------------|-----------------|
| 1 | Modification of an entity by a non-author | Error: operation not allowed |
| 2 | Creating a lab work based on an existing one | Copy contains the original’s `sourceId` |
| 3 | Creating a lecture based on an existing one | Copy contains the original’s `sourceId` |
| 4 | Attempt to modify lab work score | Error: modification not allowed |
| 5 | Creating a subject with total score ≠ 100 | Error: invalid score distribution |
| 6 | Attempt to modify a subject by a non-author | Error: operation not allowed |
| 7 | Creating a program with subjects distributed by semesters | Program successfully created |
| 8 | Automatic author assignment for new entities | Author correctly assigned |

---

## Technologies
- **C# (.NET 8)**
- **xUnit** — unit testing  
- Patterns: **Prototype**, **Builder**, **Factory Method**, **Repository**

---

## Result
A flexible model for educational program design with cloning, inheritance, and author verification features.  
The use of creational design patterns ensures scalability and reusability of the solution.
