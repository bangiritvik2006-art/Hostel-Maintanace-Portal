# Architecture

```text
React UI (localhost:5173)
        |
        | HTTP / JSON REST
        v
Spring Boot Main API (localhost:8080)
  Controller -> Service -> Repository -> MySQL
        |
        | HTTP event when request status changes
        v
Notification API (localhost:8081)
```

## Layers
- Presentation: React pages, reusable form and request components.
- API/controller: Spring MVC controllers expose REST endpoints.
- Business: `RequestService` applies request lifecycle rules.
- Domain/data: JPA entities and Spring Data repositories.
- Persistence: MySQL tables `maintenance_requests` and `staff_members`.

## ER relationship
`staff_members (1) -------- (many) maintenance_requests`

A staff member can be assigned multiple requests. A request can have zero or one assigned staff member.
