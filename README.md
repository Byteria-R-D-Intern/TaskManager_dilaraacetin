# Task Manager 

A small, clean task manager built with **Spring Boot (JWT, JPA)** and a **vanilla HTML/CSS/JS** frontend. It supports user auth, role-based access (USER/MANAGER/ADMIN), task CRUD (with filters), comments, action logs, and simple stats.

---

## Features

- **JWT auth** (login/register) + method-level authorization  
- **Roles:** `ROLE_USER`, `ROLE_MANAGER`, `ROLE_ADMIN`  
- **Tasks:** create, list (own), list all (manager/admin), delete, filter (status/priority/title)  
- **Comments:** add/list per task (owner-only)  
- **Stats:** per-user; global (manager/admin)  
- **Action Logs:** latest actions (admin-only UI)  

---

## Tech Stack

- **Backend:** Java 17+, Spring Boot, Spring Security, JPA/Hibernate  
- **Auth:** JWT (HS256)  
- **DB:** Any JPA-supported DB  
- **Docs:** OpenAPI/Swagger  
- **Frontend:** Plain HTML/CSS/JS (no framework)

---

##  Project Layout (key parts)

```
backend/
  src/main/java/com/example/taskmanager/
    adapters/web/…           # REST controllers (Auth, Tasks, Stats, Admin, Logs, Users, Comments)
    application/usecases/…   # Use cases (services)
    domain/model|ports|…     # Entities, enums, interfaces
    infrastructure/…         # JPA entities/mappers/repositories
    config/…                 # Security, JWT, OpenAPI
    TaskmanagerApplication.java

frontend/
  auth.html                  # Login/Register (double panel)
  menu.html                  # Main menu after login
  tasks.html                 # My tasks (list + filters + delete)
  create-task.html           # Create new task
  all-tasks.html             # All users’ tasks (manager/admin)
  users-admin.html           # Admin user management (role update, delete)
  stats.html                 # My stats (+ All stats for manager/admin)
  (optional) logs.html       # Admin: latest logs

  css/
    auth.css
    menu.css
    tasks.css                

  js/
    auth-combined.js         # Login/Register actions
    menu.js                  # Menu wiring
```

---

## Backend Setup

### 1) Prerequisites
- Java 17+
- Maven 3.8+
- A database 

### 2) Configure Environment

Create the file: `src/main/resources/application.properties`

```properties
# Database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT settings
jwt.secret=your_secret_key
jwt.expiration=3600000
```

Alternatively, you can use a `.env` file:

```env
DB_URL=jdbc:mysql://localhost:3306/taskmanager_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=3527

JWT_SECRET=your_secret_key
JWT_EXPIRATION=3600000

```


> **Important:** `jwt.secret` must be **≥ 32 bytes** for HS256 (`Keys.hmacShaKeyFor`).

### 3) Run the backend

```bash
mvn spring-boot:run
```
- API base: `http://localhost:8080/api`  
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
---

## Frontend Setup

Serve the `frontend/` folder with any static server (e.g., VS Code Live Server). Start from **`auth.html`**.

---

## Security Notes

**Server-side (Spring Security):**
- JWT filter + method-level guards (`@PreAuthorize`)
- Use `hasRole('ADMIN')` (not `hasRole('ROLE_ADMIN')`) in annotations
- Passwords stored with **BCrypt**; on profile updates, re-encode non-bcrypt passwords
- Unified 401/403 JSON response possible via a custom authentication entry point & access denied handler
- CORS restricted via `app.cors.allowed-origins`

**Client-side (guard):**
- Blocks direct opening of static pages without login or correct role
- Prevents secured content from flashing before the guard runs

> Server-side protection is the **source of truth**. The client guard improves UX and blocks casual access to static files.

---

##  User Flows

- **Auth (auth.html)**  
  Register → Login → token saved to `localStorage.jwtToken` → redirect to `menu.html`.

- **Menu (menu.html)**  
  Shows shortcuts. Some items visible only to `MANAGER/ADMIN` or `ADMIN`.

- **My Tasks (tasks.html)**  
  Lists **current user** tasks. Filters by **Status / Priority / Title**, delete own task(s).

- **Create Task (create-task.html)**  
  Adds a new task (title, description, status, priority, due date).

- **All Tasks (all-tasks.html)** *(MANAGER/ADMIN)*  
  Lists all users’ tasks with filters and delete.

- **Users Admin (users-admin.html)** *(ADMIN)*  
  - Fetch all users  
  - Filter/search (id/username/email/role)  
  - Update role (USER/MANAGER/ADMIN)  
  - Delete user  
  - **Refresh** button to re-fetch

- **Stats (stats.html)**  
  - **My Stats:** total / TODO / IN_PROGRESS / DONE with progress bars  
  - **All Users:** visible to MANAGER/ADMIN

- **Logs (logs.html)** *(ADMIN, optional)*  
  View latest action logs (e.g., last 30).

---

##  API Quick Reference

> Auth header: `Authorization: Bearer <token>`

### Auth

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","email":"a@ex.com","password":"secret123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"a@ex.com","password":"secret123"}'
# => {"token":"..."}
```

### Tasks (self)

```bash
# Create
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"title":"Report","description":"Q3","status":"TODO","priority":"HIGH","dueDate":"2025-09-01"}'

# List (with optional filters)
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/tasks?status=TODO&priority=HIGH&title=rep"

# Delete
curl -X DELETE http://localhost:8080/api/tasks/{taskid} \
  -H "Authorization: Bearer $TOKEN"
```

### All Tasks (manager/admin)

```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/tasks/all
```

### Comments

```bash
# Add (only if you own the task)
curl -X POST http://localhost:8080/api/tasks/{taskid}/comments \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"content":"Looks good"}'

# List
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/tasks/{taskid}/comments
```

### Stats

```bash
# My stats
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/stats/tasks

# All stats (manager/admin)
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/stats/tasks/all
```

### Users

```bash
# Get user
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/users

# Update user
curl -X PUT http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"username":"alice","email":"a@ex.com","password":"newpass"}'

# Admin: delete user
curl -X DELETE http://localhost:8080/api/users/{userid} -H "Authorization: Bearer $TOKEN"

# Admin: update role
curl -X PUT http://localhost:8080/api/admin/users/{userid}/role \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"role":"ROLE_MANAGER"}'
```

### Logs (admin)

```bash
# All logs 
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/logs

# Logs by user
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/logs/7
```

---

##  First Admin 

Since role updates require an admin, the **first admin** must be set manually.
