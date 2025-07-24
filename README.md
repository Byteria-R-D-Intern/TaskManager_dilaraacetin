# 🗂️ TaskManager API

A personal task management REST API built with **Spring Boot**, **MySQL**, **JWT authentication**, and **Clean Architecture** principles.

---

## 📦 Installation & Run

### 1. Clone the repository

  git clone https://github.com/YOUR_USERNAME/taskmanager.git
  cd taskmanager

### 2. Create the MySQL database

  CREATE DATABASE taskmanager_db;

### 3. Configure application.properties

Create a file at:
src/main/resources/application.properties

properties:

spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

### 4. Build and run the project

  ./mvnw spring-boot:run
  The application should start on:
  http://localhost:8080

#### API Usage

📝 Register

POST /api/auth/register
Content-Type: application/json
Request body:
  {
    "username": "dilara",
    "email": "dilara@example.com",
    "password": "123456"
  }
