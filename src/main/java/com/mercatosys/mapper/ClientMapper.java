package com.mercatosys.mapper;

import com.mercatosys.dto.client.ClientUpdateDTO;
import com.mercatosys.entity.Client;
import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    Client toEntity(ClientRequestDTO dto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "level", target = "level")
    @Mapping(source = "totalOrder", target = "totalOrder")
    @Mapping(source = "totalSpent", target = "totalSpent")
    ClientResponseDTO toResponseDTO(Client client);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ClientUpdateDTO dto, @MappingTarget Client entity);
}

