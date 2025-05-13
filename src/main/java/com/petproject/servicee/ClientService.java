package com.petproject.servicee;

import com.petproject.dto.ClientRequestDTO;
import com.petproject.dto.ClientResponseDTO;

import java.util.List;

public interface ClientService {
    ClientResponseDTO create(ClientRequestDTO dto);
    ClientResponseDTO update(Long id, ClientRequestDTO dto);
    void delete(Long id);
    ClientResponseDTO getById(Long id);
    List<ClientResponseDTO> getAll();
}
