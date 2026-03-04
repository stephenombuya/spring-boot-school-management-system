# 🏫 School Management System

> A production-ready Spring Boot REST API for managing every aspect of a school: students, faculty, courses, attendance, grades, schedules, and communications — all secured with JWT-based role access.

---

## 🚀 What's Improved vs. the Original Spec

| Area | Original | This Implementation |
|---|---|---|
| Auth | Basic Spring Security | Stateless JWT (access + refresh tokens) |
| Validation | Mentioned | Full `@Valid` + custom patterns on every DTO |
| Error Handling | Not defined | Global handler with typed exceptions & structured errors |
| Pagination | Not defined | All list endpoints paginated with `PagedResponse<T>` |
| Soft Deletes | Not mentioned | Users and courses are soft-deleted (isActive flag) |
| Grade Logic | Store grades | Auto-calculates letter grade and GPA points on save |
| Attendance | Basic tracking | Includes summary % + per-date class roster |
| Messaging | Simple exchange | Threaded replies, read/unread tracking, soft delete by sender/receiver |
| API Docs | Swagger mentioned | Full OpenAPI 3 with bearer auth support |
| Testing | Not mentioned | Integration tests with H2 in-memory database |

---

## 🛠 Technologies

| Technology | Purpose |
|---|---|
| Java 17 | Core language |
| Spring Boot 3.2 | Framework |
| Spring Security + JWT | Authentication & authorization |
| Spring Data JPA / Hibernate | ORM |
| MySQL | Production database |
| H2 | Test database |
| Springdoc OpenAPI 2 | API documentation |
| Lombok | Boilerplate reduction |
| MapStruct | DTO mapping |
| Maven | Build tool |

---

## 📁 Package Structure

```
com.schoolmanagement
├── controller/         # REST controllers (HTTP layer)
│   ├── AuthController
│   ├── UserController
│   ├── CourseController
│   ├── EnrollmentController
│   ├── AttendanceController
│   ├── GradeController
│   ├── ScheduleController
│   └── MessageController
│
├── service/            # Business logic
│   ├── AuthService
│   ├── UserService
│   ├── CourseService
│   ├── EnrollmentService
│   ├── AttendanceService
│   ├── GradeService
│   ├── ScheduleService
│   └── MessageService
│
├── repository/         # JPA repositories
│   ├── UserRepository
│   ├── CourseRepository
│   ├── EnrollmentRepository
│   ├── AttendanceRepository
│   ├── GradeRepository
│   ├── ScheduleRepository
│   └── MessageRepository
│
├── model/              # JPA entities
│   ├── User            (with Role enum: ADMIN | TEACHER | STUDENT)
│   ├── Course
│   ├── Enrollment      (with EnrollmentStatus: ACTIVE | DROPPED | COMPLETED | PENDING)
│   ├── Attendance      (with AttendanceStatus: PRESENT | ABSENT | LATE | EXCUSED)
│   ├── Grade           (auto-calculates letter grade + GPA points on save)
│   ├── Schedule        (with ScheduleType: CLASS | EXAM | ASSIGNMENT | LAB | SEMINAR)
│   └── Message         (threaded, soft-deletable by either party)
│
├── dto/
│   ├── request/        # Validated input DTOs
│   └── response/       # Typed output DTOs (ApiResponse<T>, PagedResponse<T>)
│
├── security/           # JWT utilities, filter, UserDetailsService
├── config/             # SecurityConfig, SwaggerConfig
└── exception/          # GlobalExceptionHandler + typed exceptions
```

---

## 🗄 Database Schema

```
users          → id, first_name, last_name, email, password, role, phone, address, dob, is_active
courses        → id, name, course_code, description, credits, max_capacity, teacher_id, is_active
enrollments    → id, student_id, course_id, semester, status, enrollment_date, drop_date
attendance     → id, student_id, course_id, attendance_date, status, remarks, marked_by
grades         → id, student_id, course_id, semester, assessment_type, score, max_score, letter_grade, grade_points
schedules      → id, course_id, title, event_type, start_time, end_time, location, is_recurring
messages       → id, sender_id, receiver_id, subject, content, is_read, parent_message_id, related_course_id
```

---

## 🔐 Role-Based Access Control

| Endpoint Area | ADMIN | TEACHER | STUDENT |
|---|---|---|---|
| Register/Login | ✅ | ✅ | ✅ |
| Manage users | ✅ | ❌ | ❌ |
| View user profiles | ✅ | ✅ | self only |
| Create/update courses | ✅ | ❌ | ❌ |
| View courses | ✅ | ✅ | ✅ |
| Enroll students | ✅ | ✅ | ❌ |
| Drop a course | ✅ | ❌ | ✅ (own) |
| Mark attendance | ✅ | ✅ | ❌ |
| View attendance | ✅ | ✅ | ✅ (own) |
| Submit/update grades | ✅ | ✅ | ❌ |
| View grades/transcript | ✅ | ✅ | ✅ (own) |
| Create schedules | ✅ | ✅ | ❌ |
| View timetable | ✅ | ✅ | ✅ |
| Send/receive messages | ✅ | ✅ | ✅ |

---

## ⚙️ Setup & Installation

### Prerequisites
- Java 17+
- MySQL 8+
- Maven 3.8+

### Steps

**1. Clone the repository**
```bash
git clone <repo-url>
cd school-management-system
```

**2. Create the MySQL database**
```sql
CREATE DATABASE school_db;
```

**3. Configure database credentials**

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/school_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

**4. (Optional) Configure email**
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**5. Build and run**
```bash
mvn clean install
mvn spring-boot:run
```

**6. Access Swagger UI**
```
http://localhost:8080/api/swagger-ui.html
```

---

## 🔑 Quick Start: Authentication Flow

**Register a student:**
```http
POST /api/auth/register
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane@school.com",
  "password": "SecurePass1",
  "role": "STUDENT"
}
```

**Login:**
```http
POST /api/auth/login
{
  "email": "jane@school.com",
  "password": "SecurePass1"
}
```

**Use the returned `accessToken` as a Bearer token on all subsequent requests:**
```http
Authorization: Bearer <accessToken>
```

---

## 📡 Key API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/auth/register` | Register new user |
| POST | `/auth/login` | Login and get tokens |
| POST | `/auth/refresh` | Refresh access token |
| GET | `/users/me` | Get own profile |
| GET | `/users/students` | List all students (Admin/Teacher) |
| GET | `/courses` | Browse active courses |
| POST | `/courses` | Create course (Admin) |
| POST | `/enrollments` | Enroll student in course |
| PATCH | `/enrollments/{id}/drop` | Drop a course |
| POST | `/attendance` | Mark attendance (Teacher) |
| GET | `/attendance/summary/student/{id}/course/{id}` | Attendance % |
| POST | `/grades` | Submit grade (Teacher) |
| GET | `/grades/gpa/student/{id}/semester/{sem}` | Student GPA |
| GET | `/grades/transcript/student/{id}/semester/{sem}` | Full transcript |
| GET | `/schedules/student/{id}/timetable` | Student timetable |
| GET | `/schedules/exams/upcoming` | Upcoming exams |
| POST | `/messages` | Send message |
| GET | `/messages/inbox` | View inbox |
| GET | `/messages/conversation/{userId}` | View thread |
| GET | `/messages/unread-count` | Unread badge count |
| GET | `/users/stats` | Admin dashboard stats |

---

## 🧪 Running Tests

```bash
mvn test
```

Tests use an H2 in-memory database — no MySQL setup required.

---

## 📄 License

MIT License

## 🤝 Contribution

Contributions are welcome! Feel free to open an issue or submit a pull request.
