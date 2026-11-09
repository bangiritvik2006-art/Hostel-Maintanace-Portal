HOSTELFIX PORTAL - FULL STACK PROJECT

TECHNOLOGIES
- React + Vite frontend
- Java 17 + Spring Boot backend
- MySQL database + Spring Data JPA
- Separate Spring Boot notification API

REQUIREMENTS ON THE COMPUTER
- Node.js (LTS)
- Java JDK 17
- Maven 3.9+
- MySQL Server 8+
- MySQL Workbench (recommended)
- VS Code (recommended)

QUICK START
1. Extract this ZIP.
2. Open the extracted HostelFix-Portal folder in VS Code.
3. Make sure MySQL Server is running.
4. Open MySQL Workbench and connect to your local MySQL server.
5. Open and execute database/schema.sql.
6. Open and execute database/seed.sql.
7. Double-click START-HOSTELFIX.bat.
8. Enter your MySQL username and password when prompted.
9. Wait for all three terminal windows to finish starting.
10. Open http://localhost:5173

ONE-COMMAND START
From Command Prompt in this folder:
  START-HOSTELFIX.bat

SERVICES
- React: http://localhost:5173
- Main API: http://localhost:8080
- Notification API health: http://localhost:8081/api/notifications/health

IF THE FRONTEND DEPENDENCIES ARE NOT INSTALLED
The launcher automatically runs npm install on the first launch.

DATABASE
Database name: hostelfix
SQL files:
- database/schema.sql
- database/seed.sql

The backend uses these defaults:
- URL: jdbc:mysql://localhost:3306/hostelfix
- Username: root (launcher asks for username)
- Password: entered in launcher

The launcher sets DB_USERNAME and DB_PASSWORD for the backend process.

IMPORTANT NOTES
- MySQL Server must be running before the backend starts.
- First-time Maven startup downloads dependencies, so internet access is needed.
- First-time npm install downloads React packages, so internet access is needed.
- Do not close the terminal windows while demonstrating the application.
- Do not commit or share your real database password.
- This project is a classroom starter implementation. Test the complete workflow on the target laptop before submission.
