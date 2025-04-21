package com.petproject.entity;

import com.petproject.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @Column(nullable = false)
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @Column(unique = true, nullable = false)
    @Email(message = "Некорректный email")
    @NotBlank(message = "Email обязателен")
    private String email;

    @Column(nullable = false)
    @PastOrPresent(message = "Дата найма не может быть в будущем")
    @NotNull(message = "Дата найма обязательна")
    private LocalDate hireDate;

    @Column(nullable = false)
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    @NotBlank(message = "Пароль обязателен")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Роль обязательна")
    private Role role;

    // Конструктор без пароля и роли — если где-то нужно только базовая информация
    public Employee(Long id, String firstName, String lastName, String email, LocalDate hireDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hireDate = hireDate;
    }

    public Employee() {

    }
    // Геттеры и сеттеры для всех полей

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
