package com.petproject.controller;

import com.petproject.dto.EmployeeRequestDTO;
import com.petproject.dto.EmployeeResponseDTO;
import com.petproject.model.Role;
import com.petproject.servicee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;  // Используем Jakarta Validation

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Validated  // Включаем глобальную валидацию
public class EmployeeController {

    private final EmployeeService employeeService;

    // Создание нового сотрудника с валидацией
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Вернуть ошибки валидации в формате ответа с BAD_REQUEST
            return ResponseEntity.badRequest().body(new EmployeeResponseDTO(bindingResult.getAllErrors()));
        }
        return ResponseEntity.ok(employeeService.create(dto));
    }

    // Обновление существующего сотрудника с валидацией
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Вернуть ошибки валидации в формате ответа с BAD_REQUEST
            return ResponseEntity.badRequest().body(new EmployeeResponseDTO(bindingResult.getAllErrors()));
        }
        return ResponseEntity.ok(employeeService.update(id, dto));
    }

    // Удаление сотрудника
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Получение сотрудника по ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    // Поиск сотрудников с фильтрами
    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeResponseDTO>> searchEmployees(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String role,  // Используем String для роли
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Role roleEnum = null;
        if (role != null) {
            try {
                roleEnum = Role.valueOf(role);  // Преобразование String в роль
            } catch (IllegalArgumentException e) {
                // Если роль некорректна, возвращаем ошибку с BAD_REQUEST
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Page.empty());  // Пустая страница
            }
        }

        Page<EmployeeResponseDTO> result = employeeService.getEmployees(firstName, lastName, roleEnum, page, size, sortBy, sortDirection);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Получение всех сотрудников
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAll());
    }
}
