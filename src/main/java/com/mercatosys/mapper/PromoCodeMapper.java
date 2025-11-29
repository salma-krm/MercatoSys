package com.mercatosys.mapper;

import com.mercatosys.dto.promocode.*;
import com.mercatosys.entity.PromoCode;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {

    PromoCode toEntity(PromoCodeRequestDTO dto);

    PromoCodeResponseDTO toDTO(PromoCode promo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PromoCodeUpdateDTO dto, @MappingTarget PromoCode promo);
}
