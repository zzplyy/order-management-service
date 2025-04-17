package com.petproject.repository;

import com.petproject.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    // Для проверки уникальности email
    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);
}
