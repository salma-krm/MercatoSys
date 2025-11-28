package com.mercatosys.mapper;

import com.mercatosys.dto.promocode.PromoCodeRequestDTO;
import com.mercatosys.dto.promocode.PromoCodeResponseDTO;
import com.mercatosys.dto.promocode.PromoCodeUpdateDTO;
import com.mercatosys.entity.PromoCode;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {


    PromoCode toEntity(PromoCodeRequestDTO dto);


    PromoCodeResponseDTO toDTO(PromoCode entity);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PromoCodeUpdateDTO dto, @MappingTarget PromoCode entity);
}
