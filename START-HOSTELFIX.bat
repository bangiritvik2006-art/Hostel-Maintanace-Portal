@echo off
setlocal
title HostelFix - Start All Services
cd /d "%~dp0"

echo ==========================================
echo        HOSTELFIX - FULL STACK LAUNCHER
echo ==========================================
echo.

where node >nul 2>nul
if errorlevel 1 (
  echo ERROR: Node.js is not installed or not in PATH.
  echo Install Node.js LTS, then reopen this terminal.
  pause
  exit /b 1
)

where npm >nul 2>nul
if errorlevel 1 (
  echo ERROR: npm is not available in PATH.
  pause
  exit /b 1
)

where java >nul 2>nul
if errorlevel 1 (
  echo ERROR: Java is not installed or not in PATH.
  echo Install JDK 17 and reopen this terminal.
  pause
  exit /b 1
)

where mvn >nul 2>nul
if errorlevel 1 (
  echo ERROR: Maven is not installed or not in PATH.
  echo Install Maven and reopen this terminal.
  pause
  exit /b 1
)

where mysql >nul 2>nul
if errorlevel 1 (
  echo WARNING: mysql command was not found in PATH.
  echo Ensure MySQL Server is running and the hostelfix database
  echo has been created/imported using MySQL Workbench.
  echo.

) else (
  echo MySQL CLI detected.
)

echo Checking frontend dependencies...
if not exist "frontend\node_modules" (
  echo First run: installing frontend packages...
  pushd frontend
  call npm install
  if errorlevel 1 (
    echo ERROR: npm install failed.
    popd
    pause
    exit /b 1
  )
  popd
)

echo.
echo Enter MySQL root username. Press Enter for root:
set "DB_USERNAME=root"
set /p "DB_USERNAME=MySQL username: "
if "%DB_USERNAME%"=="" set "DB_USERNAME=root"

set "DB_PASSWORD="
set /p "DB_PASSWORD=MySQL password (leave blank if none): "

echo.
echo Starting Notification API on port 8081...
start "HostelFix - Notification API" cmd /k "cd /d ""%~dp0notification-service"" && mvn spring-boot:run"

echo Starting Spring Boot API on port 8080...
start "HostelFix - Backend API" cmd /k "cd /d ""%~dp0backend"" && set DB_USERNAME=%DB_USERNAME% && set DB_PASSWORD=%DB_PASSWORD% && mvn spring-boot:run"

echo Starting React frontend on port 5173...
start "HostelFix - React Frontend" cmd /k "cd /d ""%~dp0frontend"" && npm run dev -- --host 0.0.0.0"

echo.
echo ==========================================
echo Services are launching in separate windows.
echo Wait until startup completes, then open:
echo.
echo   Frontend:     http://localhost:5173
echo   Backend API:  http://localhost:8080/api/requests
echo   Notification: http://localhost:8081/api/notifications/health
echo.
echo IMPORTANT:
echo 1. Ensure MySQL Server is running.
echo 2. Ensure database 'hostelfix' exists.
echo 3. Import database\schema.sql and database\seed.sql
echo    in MySQL Workbench if this is the first run.
echo 4. If MySQL has a password, enter it above.
echo ==========================================
pause
endlocal
