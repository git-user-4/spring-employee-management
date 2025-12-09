package com.tu.course.employee_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    // Cloudinary Image public_id
    @Column(name = "avatar_public_id", unique = true)
    private String avatarPublicId;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL) // Set department FK to null on department delete
    private Department department;

}
