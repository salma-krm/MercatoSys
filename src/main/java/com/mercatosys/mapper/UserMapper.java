package com.mercatosys.mapper;

import com.mercatosys.entity.User;
import com.mercatosys.dto.user.UserRequestDTO;
import com.mercatosys.dto.user.UserResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserRequestDTO dto);


    UserResponseDTO toResponseDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UserRequestDTO dto, @MappingTarget User entity);
}
