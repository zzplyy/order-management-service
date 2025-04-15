package com.petproject.mapper;

import com.petproject.dto.EmployeeRequestDTO;
import com.petproject.dto.EmployeeResponseDTO;
import com.petproject.entity.Employee;
import com.petproject.model.Role;
import org.mapstruct.*;

@Mapper(componentModel = "spring", imports = Role.class)
public interface EmployeeMapper {

    // DTO в Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(Role.valueOf(dto.getRole()))") // Из-за того, что Role у меня enum, а в DTO как стринг, надо конвертировать
    Employee toEntity(EmployeeRequestDTO dto);

    // Entity в DTO
    @Mapping(target = "role", expression = "java(employee.getRole().name())")
    EmployeeResponseDTO toResponseDTO(Employee employee);
}
