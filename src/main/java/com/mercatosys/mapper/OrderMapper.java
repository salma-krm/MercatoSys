package com.mercatosys.mapper;

import com.mercatosys.dto.order.*;
import com.mercatosys.entity.Order;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class, PromoCodeMapper.class})
public interface OrderMapper {


    Order toEntity(OrderRequestDTO dto);

    OrderResponseDTO toDTO(Order entity);

    List<OrderResponseDTO> toDTOs(List<Order> orders);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(OrderUpdateDTO dto, @MappingTarget Order entity);
}
