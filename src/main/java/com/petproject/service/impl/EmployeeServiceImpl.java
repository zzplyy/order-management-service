package com.petproject.service.impl;

import com.petproject.dto.EmployeeRequestDTO;
import com.petproject.dto.EmployeeResponseDTO;
import com.petproject.entity.Employee;
import com.petproject.mapper.EmployeeMapper;
import com.petproject.repository.EmployeeRepository;
import com.petproject.exception.ResourceNotFoundException;
import com.petproject.servicee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {
        Employee employee = employeeMapper.toEntity(requestDTO);
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(saved);
    }

    @Override
    public EmployeeResponseDTO create(EmployeeRequestDTO dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Сотрудник с таким email уже существует");
        }

        if (dto.getPassword().length() < 6) {
            throw new IllegalArgumentException("Пароль должен содержать минимум 6 символов");
        }

        Employee employee = employeeMapper.toEntity(dto);
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(saved);
    }

    @Override
    public EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto) {
        Employee existing = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Сотрудник не найден."));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPassword(dto.getPassword());
        existing.setHirDate(dto.getHireDate());
        existing.setRole(Enum.valueOf(com.petproject.model.Role.class, dto.getRole()));

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
}