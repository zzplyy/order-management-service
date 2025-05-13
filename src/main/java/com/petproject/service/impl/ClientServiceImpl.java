package com.petproject.service.impl;

import com.petproject.dto.ClientRequestDTO;
import com.petproject.dto.ClientResponseDTO;
import com.petproject.entity.Client;
import com.petproject.exception.ResourceNotFoundException;
import com.petproject.mapper.ClientMapper;
import com.petproject.repository.ClientRepository;
import com.petproject.servicee.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponseDTO create(ClientRequestDTO dto) {
        Client saved = clientRepository.save(clientMapper.toEntity(dto));
        return clientMapper.toResponseDTO(saved);
    }

    @Override
    public ClientResponseDTO update(Long id, ClientRequestDTO dto) {
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Клиент не найден"));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());

        Client updated = clientRepository.save(existing);
        return clientMapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Клиент не найден");
        }
        clientRepository.deleteById(id);
    }

    @Override
    public ClientResponseDTO getById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Клиент не найден"));
        return clientMapper.toResponseDTO(client);
    }

    @Override
    public List<ClientResponseDTO> getAll() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
