package com.petproject.test.controller;

import com.petproject.controller.EmployeeController;
import com.petproject.dto.EmployeeRequestDTO;
import com.petproject.dto.EmployeeResponseDTO;
import com.petproject.model.Role;
import com.petproject.servicee.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    private EmployeeRequestDTO validEmployeeRequestDTO;

    @BeforeEach
    void setUp() {
        validEmployeeRequestDTO = new EmployeeRequestDTO();
        validEmployeeRequestDTO.setFirstName("John");
        validEmployeeRequestDTO.setLastName("Doe");
        validEmployeeRequestDTO.setEmail("john.doe@example.com");
        validEmployeeRequestDTO.setPassword("password123");
        validEmployeeRequestDTO.setRole("ADMIN");
        validEmployeeRequestDTO.setHireDate(LocalDate.now());

        mockMvc = MockMvcBuilders.standaloneSetup(new EmployeeController(employeeService)).build();
    }

    @Test
    void createEmployee_ShouldReturnCreatedEmployee() throws Exception {
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(
                1L, "John", "Doe", "john.doe@example.com", "ADMIN", LocalDate.now());

        when(employeeService.create(validEmployeeRequestDTO)).thenReturn(employeeResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                        .contentType("application/json")
                        .content("{\n" +
                                "  \"firstName\": \"John\",\n" +
                                "  \"lastName\": \"Doe\",\n" +
                                "  \"email\": \"john.doe@example.com\",\n" +
                                "  \"password\": \"password123\",\n" +
                                "  \"role\": \"ADMIN\",\n" +
                                "  \"hireDate\": \"2025-05-05\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("ADMIN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hireDate").value("2025-05-05"));
    }

    @Test
    void updateEmployee_ShouldReturnUpdatedEmployee() throws Exception {
        EmployeeResponseDTO updatedEmployeeResponseDTO = new EmployeeResponseDTO(
                1L, "John", "Doe", "john.doe@example.com", "MANAGER", LocalDate.now());

        when(employeeService.update(1L, validEmployeeRequestDTO)).thenReturn(updatedEmployeeResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/1")
                        .contentType("application/json")
                        .content("{\n" +
                                "  \"firstName\": \"John\",\n" +
                                "  \"lastName\": \"Doe\",\n" +
                                "  \"email\": \"john.doe@example.com\",\n" +
                                "  \"password\": \"password123\",\n" +
                                "  \"role\": \"MANAGER\",\n" +
                                "  \"hireDate\": \"2025-05-05\"\n" +
                                "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("MANAGER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hireDate").value("2025-05-05"));
    }

    @Test
    void getEmployee_ShouldReturnEmployee() throws Exception {
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(
                1L, "John", "Doe", "john.doe@example.com", "ADMIN", LocalDate.now());

        when(employeeService.getById(1L)).thenReturn(employeeResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("ADMIN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.hireDate").value("2025-05-05"));
    }

    @Test
    void deleteEmployee_ShouldReturnNoContent() throws Exception {
        doNothing().when(employeeService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void searchEmployees_ShouldReturnList() throws Exception {
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(
                1L, "John", "Doe", "john.doe@example.com", "ADMIN", LocalDate.now());

        List<EmployeeResponseDTO> employeeList = List.of(employeeResponseDTO);

        Page<EmployeeResponseDTO> page = new PageImpl<>(employeeList, PageRequest.of(0, 10), employeeList.size());

        when(employeeService.getEmployees(null, null, Role.ADMIN, 0, 10, "id", "asc"))
                .thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/search")
                        .param("role", "ADMIN")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("sortDirection", "asc"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].role").value("ADMIN"));
    }
}
