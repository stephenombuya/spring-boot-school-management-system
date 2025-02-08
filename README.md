# School Management System

## Overview
The School Management System is a back-end application designed to manage students, faculty, academic schedules, and other school-related operations. It provides role-based access for administrators, teachers, and students, ensuring efficient management of educational institutions.

## Technologies Used
- **Java**: Main programming language.
- **Spring Boot**: Framework for building RESTful APIs.
- **MySQL**: Database for storing school-related data.
- **Spring Data JPA**: ORM for interacting with the database.
- **Spring Security**: User authentication and authorization.
- **Swagger/OpenAPI**: API documentation.

## System Structure
### **1. Architecture Pattern**
- **Layered Architecture** (N-tier architecture)
- Main layers:
  - **Controller Layer**: Handles HTTP requests and responses.
  - **Service Layer**: Implements business logic.
  - **Repository Layer**: Interacts with the database using Spring Data JPA.
  - **Security Layer**: Manages authentication and authorization.

### **2. Package Structure**
```
com.schoolmanagement
│── controller      # Handles HTTP requests
│── service         # Contains business logic
│── repository      # Database access
│── model           # Entity classes representing database tables
│── dto             # Data Transfer Objects for API requests/responses
│── config          # Configuration files (Security, Database, etc.)
│── exception       # Custom exception handling
│── util            # Utility classes (e.g., email service, validators)
```

### **3. Database Schema**
- **Users**: Stores student, teacher, and admin details (ID, name, email, password, role, etc.).
- **Courses**: Stores details of subjects and courses (ID, name, description, credits, etc.).
- **Enrollments**: Tracks student course enrollments (ID, student, course, semester, grade).
- **Schedules**: Stores class schedules, exam dates, and assignments.
- **Attendance**: Records student attendance (ID, student, date, status).
- **Grades**: Stores students’ grades and academic performance data.
- **Communication**: Tracks messages between students and teachers.

## Features
### 1. Student & Faculty Management
- Admins can add, update, or remove student and faculty records.
- Users can update their profiles.

### 2. Timetable & Schedule Management
- Manage class schedules, exams, and assignments.
- View personal timetables.

### 3. Attendance Tracking
- Teachers can mark student attendance.
- Students can view their attendance records.

### 4. Grading System
- Teachers can input grades.
- Students can view their academic performance.

### 5. Communication System
- Students and teachers can exchange messages regarding assignments and classes.

### 6. Role-Based Access
- Different roles (Admin, Teacher, Student) with specific permissions.

## Installation & Setup
### Prerequisites
- Java 17+
- MySQL Server
- Maven

### Steps to Run
1. Clone the repository:
   ```sh
   git clone https://github.com/stephenombuya/School-Management-System
   cd school-management-system
   ```
2. Configure MySQL database in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/school_db
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```
3. Install dependencies:
   ```sh
   mvn clean install
   ```
4. Run the application:
   ```sh
   mvn spring-boot:run
   ```

## API Documentation
After starting the application, access API docs at:
```
http://localhost:8080/swagger-ui/index.html
```

## Contribution
Contributions are welcome! Feel free to submit a pull request or open an issue.

## License
This project is licensed under the MIT License.

