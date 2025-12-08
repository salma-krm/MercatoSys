package com.mercatosys.mapper;

import com.mercatosys.dto.orderItem.*;
import com.mercatosys.entity.OrderItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {


    OrderItem toEntity(OrderItemRequestDTO dto);
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemResponseDTO toDTO(OrderItem entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(OrderItemRequestDTO dto, @MappingTarget OrderItem entity);
}
