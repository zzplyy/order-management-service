package com.petproject.entity;

import com.petproject.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
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
}
