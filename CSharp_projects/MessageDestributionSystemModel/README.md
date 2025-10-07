This README is available in other languages:
[🇷🇺 version](README.ru.md)

# Message Routing System

## Purpose
To practice structural design patterns, apply **OOP**, **SOLID**, and **GRASP** principles, and develop mocking skills in automated tests.  
Implement a message delivery model for various recipient types and cover the behavior with functional unit tests.

---

## Overview
The model describes message flow within a corporate system: messages have a header, body, and priority level; topics group recipients and forward messages to them.  
Recipients can be different: user, external messenger, physical display, or groups of recipients.  
Supports priority-based filtering and delivery logging. Message read status is stored only for users.

---

## Core Components
- **Message** — header, body, priority level.  
- **Topic** — name and list of recipients; sends messages to each recipient considering their filters and logging.  
- **Recipient** — interface for receiving messages; several implementations:
  - **UserRecipient** — delivers messages to a user; stores messages with status (`unread`/`read`); can mark messages as read (only if previously unread).  
  - **MessengerRecipient** — sends text through an external system (for tests — console output / mock).  
  - **DisplayRecipient** — displays text on a device with color; holds only one message at a time (clears before displaying).  
  - **GroupRecipient** — contains multiple recipients and forwards messages to each.  
- **DisplayDriver** — abstraction for a display: clearing, setting color, writing text.  
- **Filter** — defines priority-based criteria; applied to a recipient to block undesired messages.  
- **Logger** — when enabled, logs the receipt of messages by a recipient.  
- **Repository / Registry** — optionally stores configurations of topics and recipients in memory.

---

## Patterns and Approaches Used
- **Adapter / Facade** — wrappers for external messengers and display drivers.  
- **Composite** — GroupRecipient for composing multiple recipients.  
- **Decorator** — dynamically adds filters and/or logging to recipients (alternatively, Chain of Responsibility for filtering).  
- **Repository (In-Memory)** — stores topics and recipients.  
- **Dependency Injection** — for injecting drivers/loggers in tests.  
- **Mocking** — used in tests to verify filtering, logging, and integration with external endpoints.

---

## Test Scenarios

| # | Test Case | Expected Result |
|---|------------|-----------------|
| 1 | Receiving a message by a user | Message is saved with status `unread` |
| 2 | Mark `unread` → `read` | Status changes to `read` |
| 3 | Mark already `read` message as read | Error: operation not allowed |
| 4 | Recipient filter (mock) — message below threshold | Message is not delivered to recipient |
| 5 | Recipient logging (mock) — on message receipt | Log entry is generated |
| 6 | Sending to Messenger (mock) | Expected method/output of messenger is invoked |
| 7 | Two user recipients, filter applied to one | Message below threshold: unfiltered user receives message once; filtered user does not |

---

## Technologies
- **C# (.NET 8)**
- **NUnit / xUnit** — unit tests  
- **Moq / equivalent** — dependency mocking  
- Patterns: **Composite**, **Adapter/Facade**, **Decorator**, **Repository**

---

## Result
A complete object model for message routing with multiple recipient types, supporting filtering and logging, integrating external endpoints, and covered by functional unit tests.
