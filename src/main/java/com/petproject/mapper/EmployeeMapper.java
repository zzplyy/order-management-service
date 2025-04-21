package com.petproject.mapper;

import com.petproject.dto.EmployeeRequestDTO;
import com.petproject.dto.EmployeeResponseDTO;
import com.petproject.entity.Employee;
import com.petproject.model.Role;
import org.mapstruct.*;

@Mapper(componentModel = "spring", imports = Role.class)
public interface EmployeeMapper {

    // DTO в Entity
    @Mapping(target = "id", ignore = true)  // id будет генерироваться на уровне базы данных
    @Mapping(target = "role", expression = "java(Role.valueOf(dto.getRole()))")  // Преобразование строки в Enum
    Employee toEntity(EmployeeRequestDTO dto);

    // Entity в DTO
    @Mapping(target = "id", source = "id")  // Прямое соответствие
    @Mapping(target = "firstName", source = "firstName")  // Прямое соответствие
    @Mapping(target = "lastName", source = "lastName")  // Прямое соответствие
    @Mapping(target = "email", source = "email")  // Прямое соответствие
    @Mapping(target = "hireDate", source = "hireDate")  // Прямое соответствие
    @Mapping(target = "role", expression = "java(employee.getRole().name())")  // Преобразование Enum в строку
    @Mapping(target = "validationErrors", ignore = true)  // Игнорирование ошибки валидации в обычном случае
    EmployeeResponseDTO toResponseDTO(Employee employee);

    // Если нужно отображать ошибки валидации:
    // @Mapping(target = "validationErrors", expression = "java(employee.getValidationErrors())")
}

