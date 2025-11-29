package com.mercatosys.mapper;

import com.mercatosys.entity.Client;
import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {

    @Mapping(target = "id", ignore = true)
    public abstract Client toEntity(ClientRequestDTO dto);

    // Mapping pour inclure user.id dans ClientResponseDTO
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "level", target = "level")
    @Mapping(source = "totalOrder", target = "totalOrder")
    @Mapping(source = "totalSpent", target = "totalSpent")
    public abstract ClientResponseDTO toResponseDTO(Client client);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateEntityFromDTO(ClientRequestDTO dto, @MappingTarget Client entity);
}
