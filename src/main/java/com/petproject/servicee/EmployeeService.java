package com.petproject.servicee;

import com.petproject.dto.EmployeeRequestDTO;
import com.petproject.dto.EmployeeResponseDTO;
import com.petproject.model.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

    EmployeeResponseDTO create(EmployeeRequestDTO dto);
    EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto);

    void delete(Long id);

    EmployeeResponseDTO getById(Long id);
    List<EmployeeResponseDTO> getAll();

    // Новый метод для пагинации, фильтрации и сортировки
    Page<EmployeeResponseDTO> getEmployees(String firstName, String lastName, Role role, int page, int size, String sortBy, String sortDirection);
}
