# HostelFix Portal

A full-stack hostel maintenance request management application built for the 21CSE354T Task 4 assignment.

## Stack
- React + Vite + React Router
- Java 17 + Spring Boot REST API
- MySQL + Spring Data JPA
- JUnit / Spring Boot Test
- Separate Notification API service (Spring Boot)

## Features
- Dashboard statistics and recent requests
- Create, list, search, filter, update and delete maintenance requests
- Assign requests to staff members
- Service history for resolved requests
- MySQL persistence and relational mapping
- Server-side validation and error responses
- Separate notification service API boundary

## Prerequisites
- Node.js 20+
- Java 17+
- Maven 3.9+
- MySQL 8+

## 1. Database setup
Create the database and user (or use your own MySQL credentials):

```sql
CREATE DATABASE hostelfix;
```

Import `database/schema.sql` and `database/seed.sql`, or let Spring Boot create/update the tables using `spring.jpa.hibernate.ddl-auto=update`.

Set environment variables before running the backend:
- `DB_URL` (default `jdbc:mysql://localhost:3306/hostelfix`)
- `DB_USERNAME` (default `root`)
- `DB_PASSWORD` (your MySQL password)

## 2. Run the notification service
```bash
cd notification-service
mvn spring-boot:run
```
Runs on `http://localhost:8081`. Its health endpoint is `GET /api/notifications/health`.

## 3. Run the backend
In another terminal:
```bash
cd backend
mvn spring-boot:run
```
Runs on `http://localhost:8080`. Swagger is not included; use the API list below.

## 4. Run the frontend
In another terminal:
```bash
cd frontend
npm install
npm run dev
```
Open the Vite URL shown in the terminal (normally `http://localhost:5173`).

## API endpoints
| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/requests` | List requests; optional `search`, `status`, `category` |
| GET | `/api/requests/{id}` | Request details |
| POST | `/api/requests` | Create request |
| PUT | `/api/requests/{id}` | Update request |
| PATCH | `/api/requests/{id}/status` | Update status |
| PATCH | `/api/requests/{id}/assign/{staffId}` | Assign staff |
| DELETE | `/api/requests/{id}` | Delete request |
| GET | `/api/dashboard/stats` | Dashboard counts |
| GET | `/api/staff` | List staff |
| POST | `/api/staff` | Add staff |

## Example create request
```json
{
  "reporterName": "Ananya Sharma",
  "roomNumber": "B-214",
  "category": "Electrical",
  "priority": "Medium",
  "description": "Ceiling light is not working."
}
```

## Project structure
```text
HostelFix-Portal/
├── frontend/                 React application
├── backend/                  Main Spring Boot REST API
├── notification-service/     Separate notification API
├── database/                 MySQL schema and seed data
└── docs/                     Architecture, ERD and test plan
```

## Notes
- Demo records are synthetic.
- Do not commit real passwords or API keys.
- For a public deployment, configure CORS, HTTPS, authentication/authorisation and managed database credentials.
- The notification service is intentionally simple for demonstrating a separate service/API boundary. It logs notification events; connect an email/SMS provider only if authorised.
