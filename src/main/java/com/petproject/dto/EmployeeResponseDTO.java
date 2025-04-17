package com.petproject.dto;

import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private LocalDate hireDate;
    private List<String> validationErrors; // Добавим поле для ошибок валидации

    // Конструктор для обычного ответа
    public EmployeeResponseDTO(Long id, String firstName, String lastName, String email, String role, LocalDate hireDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.hireDate = hireDate;
    }

    // Конструктор для ошибок валидации
    public EmployeeResponseDTO(List<ObjectError> errors) {
        this.validationErrors = errors.stream()
                .map(ObjectError::getDefaultMessage) // Преобразуем ошибки в сообщения
                .collect(Collectors.toList());
    }

    // Геттеры и сеттеры
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}
