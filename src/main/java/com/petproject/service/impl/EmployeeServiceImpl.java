package com.petproject.service.impl;

import com.petproject.dto.EmployeeRequestDTO;
import com.petproject.dto.EmployeeResponseDTO;
import com.petproject.entity.Employee;
import com.petproject.mapper.EmployeeMapper;
import com.petproject.model.Role;
import com.petproject.repository.EmployeeRepository;
import com.petproject.exception.ResourceNotFoundException;
import com.petproject.servicee.EmployeeService;
import com.petproject.repository.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponseDTO create(EmployeeRequestDTO dto) {
        // Валидация уникальности email
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Сотрудник с таким email уже существует");
        }

        // Валидация длины пароля
        if (dto.getPassword().length() < 6) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 6 символов");
        }

        // Валидация корректности роли
        Role role;
        try {
            role = Role.valueOf(dto.getRole());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Некорректная роль");
        }

        // Создание сотрудника
        Employee employee = employeeMapper.toEntity(dto);
        employee.setRole(role);  // Устанавливаем роль
        Employee saved = employeeRepository.save(employee);

        return employeeMapper.toResponseDTO(saved);
    }

    @Override
    public EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сотрудник не найден."));

        // Валидация уникальности email
        if (!existing.getEmail().equals(dto.getEmail()) && employeeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Сотрудник с таким email уже существует");
        }

        // Валидация длины пароля
        if (dto.getPassword().length() < 6) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 6 символов");
        }

        // Валидация корректности роли
        Role role;
        try {
            role = Role.valueOf(dto.getRole());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Некорректная роль");
        }

        // Обновление информации о сотруднике
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPassword(dto.getPassword());
        existing.setHireDate(dto.getHireDate());
        existing.setRole(role);

        Employee updated = employeeRepository.save(existing);
        return employeeMapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Сотрудник не найден.");
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public EmployeeResponseDTO getById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сотрудник не найден."));
        return employeeMapper.toResponseDTO(employee);
    }

    @Override
    public List<EmployeeResponseDTO> getAll() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<EmployeeResponseDTO> getEmployees(String firstName, String lastName, Role role, int page, int size, String sortBy, String sortDirection) {
        // Создание спецификации для фильтрации по firstName, lastName и role
        Specification<Employee> spec = Specification.where(EmployeeSpecification.hasFirstName(firstName))
                .and(EmployeeSpecification.hasLastName(lastName))
                .and(EmployeeSpecification.hasRole(role));

        // Определяем направление сортировки
        Sort sort = Sort.by(Sort.Order.asc(sortBy)); // Используй Sort.Order.asc() или .desc()
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = Sort.by(Sort.Order.desc(sortBy)); // Используем desc() для убывающей сортировки
        }

        // Создаем объект Pageable для пагинации
        Pageable pageable = PageRequest.of(page, size, sort);

        // Получаем страницу сотрудников с учетом фильтрации и пагинации
        Page<Employee> employeePage = employeeRepository.findAll(spec, pageable);

        // Преобразуем сущности в DTO
        return employeePage.map(employeeMapper::toResponseDTO);
    }
}
