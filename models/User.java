package com.schoolmanagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    
    private String phoneNumber;
    
    private String address;
    
    @Column(nullable = false)
    private boolean active = true;
    
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private Set<Communication> sentMessages = new HashSet<>();
    
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private Set<Communication> receivedMessages = new HashSet<>();
    
    public enum Role {
        ADMIN, TEACHER, STUDENT
    }
}
