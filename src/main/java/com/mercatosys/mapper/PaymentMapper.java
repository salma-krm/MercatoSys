package com.mercatosys.mapper;

import com.mercatosys.dto.payment.*;
import com.mercatosys.entity.Payment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment toEntity(PaymentRequestDTO dto);

    PaymentResponseDTO toDTO(Payment entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PaymentRequestDTO dto, @MappingTarget Payment entity);
}
