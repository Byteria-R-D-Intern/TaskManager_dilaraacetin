# 🗂️ TaskManager API

A personal task management REST API built with **Spring Boot**, **MySQL**, **JWT authentication**, **Swagger UI**, and **Clean Architecture** principles.

---

## Installation & Run

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/taskmanager.git
cd taskmanager
```

### 2. Create the MySQL database

```sql
CREATE DATABASE taskmanager_db;
```

### 3. Configure `application.properties`

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
JWT_SECRET=your_secret_key
JWT_EXPIRATION=3600000
```

---

## Run the Project

```bash
./mvnw spring-boot:run
```

The app will run at:

```
http://localhost:8080
```

---

## API Documentation (Swagger UI)

After starting the application, visit:

```
http://localhost:8080/swagger-ui/index.html
```

To test secured endpoints:

1. Click the **Authorize** button in the top-right corner.
2. Enter your token as:
   ```
   Bearer YOUR_JWT_TOKEN
   ```

---

## Authentication

### Register

`POST /api/auth/register`

```json
{
  "username": "dilara",
  "email": "dilara@example.com",
  "password": "123456",
  "role": "ROLE_USER"
}
```

### Login

`POST /api/auth/login`

```json
{
  "email": "dilara@example.com",
  "password": "123456"
}
```

**Returns:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

## User Endpoints (Requires JWT Token)

> Add this header to all user and task requests:
>
> ```
> Authorization: Bearer YOUR_JWT_TOKEN
> ```

### Get user by ID

`GET /api/users`

### Update user

`PUT /api/users`

```json
{
  "username": "newname",
  "email": "new@example.com",
  "password": "newpass"
}
```

### Delete user (Admin only)

`DELETE /api/users/{id}`

---

## Task Endpoints (Requires JWT Token)

### Create Task

`POST /api/tasks`

```json
{
  "title": "Study",
  "description": "Read Spring docs",
  "status": "TODO",
  "priority": "HIGH",
  "dueDate": "2025-08-01"
}
```

### Get Tasks for Current User

`GET /api/tasks`

### Get Tasks for All Users (Admin and Manager Only)

`GET /api/tasks/all`

### Update Task

`PUT /api/tasks/{id}`

```json
{
  "title": "Study Updated",
  "description": "Read updated docs",
  "status": "IN_PROGRESS",
  "priority": "MEDIUM",
  "dueDate": "2025-08-05"
}
```

### Delete Task

`DELETE /api/tasks/{id}`

## Action Logs (Admin Only)

Requires JWT with role ROLE_ADMIN

### Get all logs

`GET /api/logs`

Returns all user action logs (create, update, delete operations).

### Get logs for a specific user

`GET /api/logs/{id}`

Returns the action logs associated with a specific user.