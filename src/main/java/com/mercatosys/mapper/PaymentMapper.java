package com.mercatosys.mapper;

import com.mercatosys.dto.payment.*;
import com.mercatosys.entity.Payment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    Payment toEntity(PaymentRequestDTO dto);
    @Mapping(source = "reference", target = "reference")
    @Mapping(source = "paymentNumber", target = "paymentNumber")
    PaymentResponseDTO toDTO(Payment entity);


}
