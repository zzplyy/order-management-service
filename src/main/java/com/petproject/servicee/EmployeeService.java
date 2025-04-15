package com.petproject.servicee;

import com.petproject.dto.EmployeeRequestDTO;
import com.petproject.dto.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDTO create(EmployeeRequestDTO dto);
    EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto);

    void delete(Long id);

    EmployeeResponseDTO getById(Long id);
    List<EmployeeResponseDTO> getAll();
}
