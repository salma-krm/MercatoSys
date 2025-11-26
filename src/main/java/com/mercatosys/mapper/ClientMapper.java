package com.mercatosys.mapper;

import com.mercatosys.entity.Client;
import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import com.mercatosys.dto.client.ClientUpdateDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClientMapper {


    Client toEntity(ClientRequestDTO dto);

    ClientResponseDTO toResponseDTO(Client entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    void updateEntityFromDTO(ClientUpdateDTO dto, @MappingTarget Client entity);

    @AfterMapping
    default void setUserId(Client entity, @MappingTarget ClientResponseDTO dto){
        if(entity.getUser() != null){
            dto.setUserId(entity.getUser().getId());
        }
    }
}
