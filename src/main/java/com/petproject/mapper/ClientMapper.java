package com.petproject.mapper;

import com.petproject.dto.ClientRequestDTO;
import com.petproject.dto.ClientResponseDTO;
import com.petproject.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientRequestDTO dto);

    ClientResponseDTO toResponseDTO(Client client);
}
