package com.schoolmanagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@OpenAPIDefinition(
    info = @Info(
        title = "School Management System API",
        version = "1.0.0",
        description = "Comprehensive REST API for managing students, faculty, courses, attendance, grades, and communications.",
        contact = @Contact(name = "School Admin", email = "admin@school.com"),
        license = @License(name = "MIT License", url = "https://opensource.org/licenses/MIT")
    )
)
public class SchoolManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchoolManagementApplication.class, args);
    }
}
